��    �        �   �	      H     I  "   K  E  n     �  (   �  *   �  #   &     J     `  &   v     �  *   �     �     �       )        2     J     b  /   u     �     �     �     �  #   �  #     %   *      P     q     �     �     �     �  -   �       "   "  &   E  "   l     �     �     �  '   �  "        +     B  !   Y     {  !   �  %   �     �     �  !        *  5   E  *   {  .   �     �  (   �  %     %   6  0   \  &   �     �  /   �     �  >     @   G     �     �     �  3   �  ,   �          0     F     \     s     �     �     �     �     �  +   �           >     Q     g     w     �     �     �  	   �     �  %   �  /        8     V     q          �  &   �     �  !   �          *     0  1   7  /   i     �  !   �     �  ,   �  
              =     J     X     e     s     �     �  4   �  $   �          :     W     u     �     �  *   �  :   �     +      I  )   j     �  #   �  0   �              
   (      3      6      J      b      }      �   %   �      �      �   &   
!     1!     3!     C!  4   T!     �!     �!     �!  �  �!     9#  '   ;#  �  c#  )   �&  !   '  ,   6'  &   c'     �'     �'  #   �'     �'  3   �'     +(     I(     R(  @   [(     �(     �(     �(  C   �(     &)     ?)     M)     `)  '   u)  '   �)  #   �)     �)     	*     !*     8*     T*     o*  3   �*     �*  )   �*  +   +  &   1+     X+     w+  #   �+  #   �+  $   �+     ,     ,  '   ;,     c,  )   y,  *   �,  $   �,     �,  "   -     5-  A   U-  '   �-  2   �-     �-  1   .  $   6.  $   [.  7   �.  3   �.     �.  9   /     ;/  A   P/  F   �/     �/     �/     �/  4   0  3   C0     w0     �0     �0     �0     �0     �0     �0     �0     1     (1  ,   71     d1     �1     �1     �1     �1     �1     �1     �1  
   2     2  *   /2  .   Z2  #   �2     �2     �2     �2  $   �2  )   3      23  $   S3  #   x3     �3     �3  7   �3  5   �3     4     $4  ,   ?4  ;   l4     �4  !   �4     �4     �4     �4     
5     5  %   55  &   [5  B   �5  .   �5  &   �5  '   6  &   C6      j6  )   �6     �6  3   �6  ;   7  (   B7  )   k7  6   �7  $   �7  ,   �7  /   8     N8     ^8  
   n8     y8     ~8     �8     �8     �8     �8  (   �8     '9     D9  /   W9     �9     �9     �9  1   �9     �9     �9     �9     >   �          C                     U      �   ;   9   �          w       ^      
   ]   -       V   D   	   O             7           �   �   8   �   +                 "   }   ?   *      M           �   �       0           \   �   n   o   s   �       �      v         �   �   G   /   I               2   k   T   5   F   �                      1                                _   [   Q       �      �   W          x   a              6      m           �   H       c   i       g   #   .   ~   {   N   �   E      z   :   �   J       &   �   L      t       q       '           <       )   �       f   e   B   A   Z                  4   �   y       Y   �   b   `   p   (   ,      l   r   %   =      S   h   !   |   �   �   @   �   �   3       �   P   j   �       �   K   u      X   $               R      d   �    
 
Error applying application rules. 
Usage: %(progname)s %(command)s

%(commands)s:
 %(enable)-31s enables the firewall
 %(disable)-31s disables the firewall
 %(default)-31s set default policy
 %(logging)-31s set logging to %(level)s
 %(allow)-31s add allow %(rule)s
 %(deny)-31s add deny %(rule)s
 %(reject)-31s add reject %(rule)s
 %(limit)-31s add limit %(rule)s
 %(delete)-31s delete %(urule)s
 %(insert)-31s insert %(urule)s at %(number)s
 %(status)-31s show firewall status
 %(statusnum)-31s show firewall status as numbered list of %(rules)s
 %(statusverbose)-31s show verbose firewall status
 %(show)-31s show firewall report
 %(version)-31s display version information

%(appcommands)s:
 %(applist)-31s list application profiles
 %(appinfo)-31s show information on %(profile)s
 %(appupdate)-31s update %(profile)s
 %(appdefault)-31s set default application policy
  (skipped reloading firewall)  Attempted rules successfully unapplied.  Proceed with operation (%(yes)s|%(no)s)?   Some rules could not be unapplied. %s is group writable! %s is world writable! '%(f)s' file '%(name)s' does not exist '%s' does not exist (be sure to update your rules accordingly) : Need at least python 2.5)
 Aborted Action Adding IPv6 rule failed: IPv6 not enabled Available applications: Bad destination address Bad interface name Bad interface name: can't use interface aliases Bad interface type Bad port Bad port '%s' Bad source address Cannot insert rule at position '%d' Cannot insert rule at position '%s' Cannot specify 'all' with '--add-new' Cannot specify insert and delete Checking ip6tables
 Checking iptables
 Checking raw ip6tables
 Checking raw iptables
 Checks disabled Command may disrupt existing ssh connections. Could not back out rule '%s' Could not delete non-existent rule Could not find a profile matching '%s' Could not find executable for '%s' Could not find profile '%s' Could not find protocol Could not load logging rules Could not normalize destination address Could not normalize source address Could not perform '%s' Could not set LOGLEVEL Could not update running firewall Couldn't find '%s' Couldn't find parent pid for '%s' Couldn't find pid (is /proc mounted?) Couldn't open '%s' for reading Couldn't stat '%s' Couldn't update application rules Couldn't update rules file Default %(direction)s policy changed to '%(policy)s'
 Default application policy changed to '%s' Default: %(in)s (incoming), %(out)s (outgoing) Description: %s

 Duplicate profile '%s', using last found ERROR: this script should not be SGID ERROR: this script should not be SUID Firewall is active and enabled on system startup Firewall not enabled (skipping reload) Firewall reloaded Firewall stopped and disabled on system startup Found exact match Found multiple matches for '%s'. Please use exact profile name Found non-action/non-logtype match (%(xa)s/%(ya)s %(xl)s/%(yl)s) From IPv6 support not enabled Improper rule syntax Improper rule syntax ('%s' specified with app rule) Insert position '%s' is not a valid position Invalid '%s' clause Invalid 'from' clause Invalid 'port' clause Invalid 'proto' clause Invalid 'to' clause Invalid IP version '%s' Invalid interface clause Invalid log level '%s' Invalid log type '%s' Invalid option Invalid policy '%(policy)s' for '%(chain)s' Invalid ports in profile '%s' Invalid position ' Invalid position '%d' Invalid profile Invalid profile name Invalid token '%s' Logging disabled Logging enabled Logging:  Missing policy for '%s' Mixed IP versions for 'from' and 'to' Must specify 'tcp' or 'udp' with multiple ports Need 'from' or 'to' with '%s' Need 'to' or 'from' clause New profiles: No match No ports found in profile '%s' No rules found for application profile Option 'log' not allowed here Option 'log-all' not allowed here Port ranges must be numeric Port: Ports: Profile '%(fn)s' has empty required field '%(f)s' Profile '%(fn)s' missing required field '%(f)s' Profile: %s
 Profiles directory does not exist Protocol mismatch (from/to) Protocol mismatch with specified protocol %s Rule added Rule changed after normalization Rule deleted Rule inserted Rule updated Rules updated Rules updated (v6) Rules updated for profile '%s' Skipped reloading firewall Skipping '%(value)s': value too long for '%(field)s' Skipping '%s': also in /etc/services Skipping '%s': couldn't process Skipping '%s': couldn't stat Skipping '%s': field too long Skipping '%s': invalid name Skipping '%s': name too long Skipping '%s': too big Skipping '%s': too many files read already Skipping IPv6 application rule. Need at least iptables 1.4 Skipping adding existing rule Skipping inserting existing rule Skipping malformed tuple (bad length): %s Skipping malformed tuple: %s Skipping unsupported IPv6 '%s' rule Status: active
%(log)s
%(pol)s
%(app)s%(status)s Status: active%s Status: inactive Title: %s
 To Unknown policy '%s' Unsupported action '%s' Unsupported default policy Unsupported direction '%s' Unsupported policy '%s' Unsupported policy for direction '%s' Unsupported protocol '%s' Wrong number of arguments You need to be root to run this script n problem running running ufw-init uid is %(uid)s but '%(path)s' is owned by %(st_uid)s unknown y yes Project-Id-Version: ufw
Report-Msgid-Bugs-To: FULL NAME <EMAIL@ADDRESS>
POT-Creation-Date: 2009-08-25 10:20-0500
PO-Revision-Date: 2009-08-25 12:31+0000
Last-Translator: Daniel Nylander <yeager@ubuntu.com>
Language-Team: Swedish <sv@li.org>
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Launchpad-Export-Date: 2009-08-26 05:56+0000
X-Generator: Launchpad (build Unknown)
 
 
Fel vid tillämpning av programregler. 
Användning: %(progname)s %(command)s

%(commands)s:
 %(enable)-31s aktiverar brandväggen
 %(disable)-31s inaktiverar brandväggen
 %(default)-31s ange standardpolicy
 %(logging)-31s ställ in loggning till %(level)s
 %(allow)-31s lägg till allow %(rule)s
 %(deny)-31s lägg till deny %(rule)s
 %(reject)-31s lägg till reject %(rule)s
 %(limit)-31s lägg till limit %(rule)s
 %(delete)-31s ta bort %(urule)s
 %(insert)-31s infoga %(urule)s på %(number)s
 %(status)-31s visa brandväggsstatus
 %(statusnum)-31s visa brandväggsstatus som numrerad lista för %(rules)s
 %(statusverbose)-31s visa informativ brandväggsstatus
 %(show)-31s visa brandväggsrapport
 %(version)-31s visa versionsinformation

%(appcommands)s:
 %(applist)-31s lista programprofiler
 %(appinfo)-31s visa information om %(profile)s
 %(appupdate)-31s uppdatera %(profile)s
 %(appdefault)-31s ange standardpolicy för program
  (hoppade över omläsning av brandvägg)  Inaktivering av regler lyckades.  Fortsätt med åtgärden (%(yes)s|%(no)s)?   Några regler kunde inte inaktiveras. %s är skrivbar för gruppen! %s är skrivbar för alla! "%(f)s"-filen "%(name)s" finns inte "%s" finns inte (glöm inte att uppdatera dina regler enligt detta) : Behöver minst python 2.5)
 Avbruten Åtgärd Misslyckades med att lägga till IPv6-regel: IPv6 inte aktiverat Tillgängliga program: Felaktig måladress Felaktigt gränssnittsnamn Felaktigt gränssnittsnamn: kan inte använd alias för gränssnitt Felaktig gränssnittstyp Felaktig port Felaktig port "%s" Felaktig källadress Kan inte infoga regel på position "%d" Kan inte infoga regel på position "%s" Kan inte ange "all" med "--add-new" Kan inte ange insert och delete Kontrollerar ip6tables
 Kontrollerar iptables
 Kontrollerar rå ip6tables
 Kontrollerar rå iptables
 Kontroller inaktiverade Kommandot kan påverka befintliga ssh-anslutningar. Kunde inte backa ut regeln "%s" Kunde inte ta bort icke-existerande regel Kunde inte hitta en profil som matchar "%s" Kunde inte hitta körbar fil för "%s" Kunde inte hitta profilen "%s" Kunde inte hitta protokollet Kunde inte läsa in loggningsregler Kunde inte normalisera måladressen Kunde inte normalisera källadressen Kunde inte genomföra "%s" Kunde inte ställa in LOGLEVEL Kunde inte uppdatera brandvägg i drift Kunde inte hitta "%s" Kunde inte hitta föräldra-pid för "%s" Kunde inte hitta pid (är /proc monterad?) Kunde inte öppna "%s" för läsning Kunde inte köra stat på '%s' Kunde inte uppdatera programregler Kunde inte uppdatera regelfilen Standardpolicy för %(direction)s har ändrats till "%(policy)s"
 Standardprogrampolicy ändrad till "%s" Standard: %(in)s (inkommande), %(out)s (utgående) Beskrivning: %s

 Dublett av profilen "%s", använder senast hittad FEL: detta skript ska inte vara SGID FEL: detta skript ska inte vara SUID Brandväggen är aktiv och aktiverad vid systemuppstart Brandvägg inte aktiverad (hoppar över omläsning) Brandväggen omläst Brandvägg stoppad och inaktiverad vid systemets uppstart Hittade exakt träff Hittade flera träffar för "%s". Använd det exakta profilnamnet Hittade non-action/non-logtype-matchning (%(xa)s/%(ya)s %(xl)s/%(yl)s) Från IPv6-stöd inte aktiverat Felaktig regelsyntax Felaktig regelsyntax ("%s" angiven med programregel) Inmatningsposition "%s" är inte en giltig position Ogiltig "%s"-klausul Ogiltig "from" Ogiltig "port" Ogiltig "proto" Ogiltig "to" Ogiltig IP-version "%s" Ogiltigt gränssnittsklausul Ogiltig loggnivå "%s" Ogiltig loggtyp "%s" Ogiltig flagga Ogiltig policy "%(policy)s" för "%(chain)s" Ogiltiga portar i profilen "%s" Ogiltig position " Ogiltig position "%d" Ogiltig profil Ogiltigt profilnamn Ogiltigt token "%s" Loggning inaktiverad Loggning aktiverad Loggning:  Saknar policy för "%s" Blandade IP-versioner för "from" och "to" Måste ange "tcp" eller "udp" med flera portar Behöver "from" eller "to" med "%s" Behöver "to" eller "from" Nya profiler: Ingen träff Inga portar hittades i profilen "%s" Inga regler hittades för programprofilen Flaggan "log" tillåts inte här Flaggan "log-all" tillåts inte här Portintervall måste vara numeriska Port: Portar: Profilen "%(fn)s" har nödvändiga fältet "%(f)s" tomt Profilen "%(fn)s" saknar nödvändiga fältet "%(f)s" Profil: %s
 Profilkatalogen finns inte Protokollen stämmer inte överens (from/to) Protokollen stämmer inte överens med angivet protokoll %s Regel lades till Regel ändrad efter normalisering Regel borttagen Regel infogad Regel uppdaterad Regler uppdaterade Regler uppdaterade (v6) Regler uppdaterade för profilen "%s" Hoppade över omläsning av brandvägg Hoppar över "%(value)s": värdet är för långt för "%(field)s" Hoppar över "%s": finns även i /etc/services Hoppar över "%s": kunde inte behandla Hoppar över "%s": kunde inte ta status Hoppar över "%s": fältet för långt Hoppar över "%s": ogiltigt namn Hoppar över "%s": namnet är för långt Hoppar över "%s": för stor Hoppar över "%s": för många filer inlästa redan Hoppar över IPv6-programregel. Behöver minst iptables 1.4 Hoppar över addering av befintlig regel Hoppar över inmatning av befintlig regel Hoppar över felformulerad tupel (felaktig längd): %s Hoppar över felformulerad tupel: %s Hoppar över IPv6 "%s"-regel som inte stöds Status: aktiv
%(log)s
%(pol)s
%(app)s%(status)s Status: aktiv%s Status: inaktiv Titel: %s
 Till Okänd policy "%s" Åtgärden "%s" stöds inte Standardpolicyn stöds inte Riktningen stöds inte "%s" Policyn '%s' stöds inte Policyn för riktningen "%s" stöds inte Protokollet "%s" stöds inte Fel antal argument Du måste vara root för att köra detta skript n kunde inte köra kör ufw-init uid är %(uid)s men "%(path)s" ägs av %(st_uid)s okänd j ja 