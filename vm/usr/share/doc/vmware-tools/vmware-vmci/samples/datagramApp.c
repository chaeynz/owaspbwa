/*
 * Copyright 2007-2008.  VMware, Inc.  The use of this code is subject to 
 * your agreement to the terms of an applicable VMware license.
 */

/*
 * datagramApp.c --
 *
 *    A sample client-server application using the Virtual Machine
 *    Communication Interface (VMCI) datagram API.
 *    Implements message passing between the client and the server in a
 *    ping-pong fashion.
 */


#include <stdlib.h>
#include <stdio.h>

#ifdef _WIN32
#  include <windows.h>
#elif defined(__linux__) || defined(__APPLE__)
#  include <unistd.h>
#else
#   error "Unsupported platform"
#endif

#include "vmcilib.h"

#define HOST_DGM_SERVICE "HOST INFO"
#define BUFSIZE 4096
#define SERVER_LOOP 3
#define REPLY "HELLO, CLIENT!"
#define QUERY "HELLO, SERVER!"

static char *devName = NULL;
static unsigned int isServer = 0;
static unsigned int isClient = 0;

static void ParseCommandLine(int argc, char *argv[]);
static void PrintUsageExit(const char *prog);
static void DoDatagramServer(void);
static void DoDatagramClient(void);
static void PrintVMCIDatagramMsg(const char *msg, unsigned int len);


/*
 *-----------------------------------------------------------------------------
 *
 * ParseCommandLine --
 *
 *      Parse the command line input parameters.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
ParseCommandLine(int argc,     // IN:
                 char *argv[]) // IN:
{
#ifdef _WIN32
   int index = 1;

   if (argc != 2 && argc != 4) {
      PrintUsageExit(argv[0]);
   }

   if (strcmp(argv[1], "-d") == 0) {
      devName = argv[2];
      index = 3;
   }

   if (strcmp(argv[index], "-s") == 0) {
      isServer = 1;
   } else if (strcmp(argv[index], "-c") == 0) {
      isClient = 1;
   } else {
      PrintUsageExit(argv[0]);
   }

#else // __linux__
   char *optstring = "d:sch";

   for (;;) {
      int c;

      c = getopt(argc, argv, optstring);
      if (c == -1) {
         break;
      }

      switch (c) {
         case 'd':
            devName = optarg;
            break;
         case 's':
            isServer = 1;
            break;
         case 'c':
            isClient = 1;
            break;
         case 'h':
         case '?':
         default:
            PrintUsageExit(argv[0]);
            break;
      }
   }
#endif
}


/*
 *-----------------------------------------------------------------------------
 *
 * PrintUsageExit --
 *
 *      Prints program usage information and exits.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
PrintUsageExit(const char *prog) // IN:
{
   fprintf(stderr, "%s [-d <VMCI device>] -c|-s\n", prog);
   fprintf(stderr, "\t\tMust provide exactly one of -c OR -s.\n");
   fprintf(stderr, "%s -h:", prog);
   fprintf(stderr, "\tPrints this help message.\n");
   exit(-1);
}


int
main(int argc, char *argv[])
{

   ParseCommandLine(argc, argv);
   
   if ((!isServer && !isClient) || (isServer && isClient)) {
      PrintUsageExit(argv[0]);
   }

   if (VMCI_InitApp(devName) != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to init VMCI library.\n");
      exit(-2);
   }

   if (isServer) {
      DoDatagramServer();
   }
   else {
      DoDatagramClient();
   }

   VMCI_CleanupApp();

   return 0;
}


/*
 *-----------------------------------------------------------------------------
 *
 * DoDatagramServer --
 *
 *      Implements the server side of the sample application. The server
 *      creates a datagram handle and waits for messages from clients. Sends a
 *      reply back to the client. Prints the message obtained from the client.
 *
 *      Uses the discovery service API to register the server handle under a
 *      known name.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
DoDatagramServer(void)
{
   int rv;
   VMCIHandle serverHandle;
   VMCIHandle publicGroupHandle;
   int j;

   serverHandle = VMCIDatagram_CreateHnd(0);
   if (VMCI_HANDLE_EQUAL(serverHandle, VMCI_INVALID_HANDLE)) {
      fprintf(stderr, "Failed to create server datagram handle.\n");
      return;
   }

   rv = VMCIDs_Lookup(VMCI_PUBLIC_GROUP_NAME, &publicGroupHandle);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to lookup public group handle.\n");
      goto out;
   }

   rv = VMCIDs_Register(HOST_DGM_SERVICE, serverHandle);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to register discovery service. rv = %d\n", rv);
      goto out;
   }

   printf("Running datagram server --- will respond to %d queries.\n",
           SERVER_LOOP);
   for (j = 0; j < SERVER_LOOP; j++) {
      /* Server loop */
      char buf[BUFSIZE];
      VMCIHandle clientHandle;

      rv = VMCIDatagram_RecvFrom(serverHandle, &clientHandle, buf, sizeof buf);
      if (rv < 0) {
         fprintf(stderr, "Error in VMCIDatagram_Read.\n");
         break;
      }
      PrintVMCIDatagramMsg(buf, rv);
      rv = VMCIDatagram_SendTo(clientHandle, serverHandle, sizeof REPLY - 1,
                             REPLY);
      if (rv < VMCI_SUCCESS) {
         fprintf(stderr, "Error in VMCIDatagram_Send.\n");
         break;
      }
   }

   VMCIDs_Unregister(HOST_DGM_SERVICE);
out:
   VMCIDatagram_DestroyHnd(serverHandle);
}


/*
 *-----------------------------------------------------------------------------
 *
 * DoDatagramClient --
 *
 *      Implements the client side of the sample application. The client
 *      creates a datagram handle and sends a message to the server. Reads the
 *      reply back from the server. Prints the message obtained from the server.
 *
 *      Uses the discovery service API to lookup the server's handle.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
DoDatagramClient(void)
{
   int rv;
   VMCIHandle serverHandle, clientHandle;
   VMCIId serverId;
   char buf[BUFSIZE];

   rv = VMCIDs_Lookup(HOST_DGM_SERVICE, &serverHandle);
   if (rv != VMCI_SUCCESS) {
      fprintf(stderr, "Failed to lookup in discovery service.\n");
      return;
   }
   serverId = VMCI_HANDLE_TO_CONTEXT_ID(serverHandle);

   clientHandle = VMCIDatagram_CreateHnd(0);
   if (VMCI_HANDLE_EQUAL(clientHandle, VMCI_INVALID_HANDLE)) {
      fprintf(stderr, "Failed to create client datagram handle.\n");
      return;
   }

   rv = VMCIDatagram_SendTo(serverHandle, clientHandle, sizeof QUERY - 1, QUERY);
   /* Wait for message from server. */
   rv = VMCIDatagram_RecvFrom(clientHandle, NULL, buf, sizeof buf);
   if (rv >= VMCI_SUCCESS) {
      PrintVMCIDatagramMsg(buf, rv);
   }

   VMCIDatagram_DestroyHnd(clientHandle);
}


/*
 *-----------------------------------------------------------------------------
 *
 * PrintVMCIDatagramMsg --
 *
 *      Prints a given number of characters from a buffer.
 *
 * Results:
 *      None.
 *
 *-----------------------------------------------------------------------------
 */

static void
PrintVMCIDatagramMsg(const char *msg,  // IN: Pointer to buffer
                     unsigned int len) // IN: Number of bytes to print
{
   unsigned int i;

   printf("Received message: ");
   for (i = 0; i < len; i++) {
      printf("%c", msg[i]);
   }
   printf("\n");
}
