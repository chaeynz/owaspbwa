��    �      �  �   l	      �  "   �  E  �     �  (     *   9  #   d     �     �  &   �     �  *   �          7     ?  )   F     p     �     �  /   �     �     �     �       #      #   D  %   h      �     �     �     �     �       -        C  "   `  &   �  "   �     �     �       '     "   F     i     �  !   �     �  !   �  %   �          3  !   F     h  *   �     �  (   �  %   �  %     0   5  &   f     �  /   �     �  >   �  @         a     f       3   �  ,   �     �          !     8     L     d     }     �     �     �     �     �                %     8     I  	   Y  %   c  /   �     �     �     �           	  &   (     O  !   m     �     �     �  1   �  /   �       !   '     I  ,   e  
   �      �     �     �     �     �     �          &  4   A  $   v     �     �     �     �          /  *   F  :   q     �      �  )   �       #   2  0   V     �     �  
   �     �     �     �     �     �       &   /     V     X     h  4   y     �     �     �  �  �  0   X!  �  �!  0   !%  ,   R%  ,   %  -   �%  "   �%  %   �%  ,   #&     P&  :   d&     �&  	   �&  	   �&  4   �&     '     '     7'  <   P'     �'     �'     �'     �'  5   �'  /   (  0   E(  2   v(     �(     �(  '   �(  &   )     ))  4   ;)  )   p)  '   �)  3   �)  )   �)  %    *     F*  2   e*  -   �*  -   �*      �*      +  .   6+     e+      z+  &   �+  -   �+  %   �+  0   ,  -   G,  5   u,     �,  =   �,  #   �,  #   -  4   =-  ?   r-     �-  :   �-     .  O    .  T   p.     �.     �.     �.  D    /  6   E/     |/     �/     �/     �/     �/     0  +   )0     U0     u0  "   �0     �0     �0     �0     �0     1     1     .1     ?1  (   Q1  >   z1  ,   �1  -   �1     2  	   #2  1   -2  8   _2  $   �2  (   �2  $   �2     3     3  =   3  5   V3     �3     �3     �3  ,   �3     �3  $   4     44     F4     Y4     q4     �4  )   �4  -   �4  L    5  -   M5  0   {5  ,   �5  +   �5  (   6  -   .6  (   \6  @   �6  M   �6  -   7  .   B7  2   q7      �7  3   �7  1   �7     +8     <8     M8     Y8     \8  "   w8      �8      �8     �8  9   �8     09     29     L9  5   b9     �9     �9     �9     -   ^               ;   w       h      �      R   G   m   =   X      �   {   d          z                  P   y   !              *                         �   E             s   �      /   �   �   L   W       #   �           5   )   �   �       O   0   q   u   	   1   �      a   �   j   Q   �       T               b   Z   "           <   f   H              n   '   .   7   D      t   F      >   ?   ~           9      �       e   \   x      $   i   @       2   �   �   `   A   J       r       �          ]             �       
   S       k   8   (         g   o   %      �           +      I   N      �   �   Y          &   �   [   3   K   U   V   6   }       l       ,   :   p   C       �   |       c       B           4           _             M      v        
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
 Checks disabled Command may disrupt existing ssh connections. Could not back out rule '%s' Could not delete non-existent rule Could not find a profile matching '%s' Could not find executable for '%s' Could not find profile '%s' Could not find protocol Could not load logging rules Could not normalize destination address Could not normalize source address Could not perform '%s' Could not set LOGLEVEL Could not update running firewall Couldn't find '%s' Couldn't find parent pid for '%s' Couldn't find pid (is /proc mounted?) Couldn't open '%s' for reading Couldn't stat '%s' Couldn't update application rules Couldn't update rules file Default application policy changed to '%s' Description: %s

 Duplicate profile '%s', using last found ERROR: this script should not be SGID ERROR: this script should not be SUID Firewall is active and enabled on system startup Firewall not enabled (skipping reload) Firewall reloaded Firewall stopped and disabled on system startup Found exact match Found multiple matches for '%s'. Please use exact profile name Found non-action/non-logtype match (%(xa)s/%(ya)s %(xl)s/%(yl)s) From IPv6 support not enabled Improper rule syntax Improper rule syntax ('%s' specified with app rule) Insert position '%s' is not a valid position Invalid 'from' clause Invalid 'port' clause Invalid 'proto' clause Invalid 'to' clause Invalid IP version '%s' Invalid interface clause Invalid log level '%s' Invalid log type '%s' Invalid option Invalid ports in profile '%s' Invalid position ' Invalid position '%d' Invalid profile Invalid profile name Invalid token '%s' Logging disabled Logging enabled Logging:  Mixed IP versions for 'from' and 'to' Must specify 'tcp' or 'udp' with multiple ports Need 'from' or 'to' with '%s' Need 'to' or 'from' clause New profiles: No match No ports found in profile '%s' No rules found for application profile Option 'log' not allowed here Option 'log-all' not allowed here Port ranges must be numeric Port: Ports: Profile '%(fn)s' has empty required field '%(f)s' Profile '%(fn)s' missing required field '%(f)s' Profile: %s
 Profiles directory does not exist Protocol mismatch (from/to) Protocol mismatch with specified protocol %s Rule added Rule changed after normalization Rule deleted Rule inserted Rule updated Rules updated Rules updated (v6) Rules updated for profile '%s' Skipped reloading firewall Skipping '%(value)s': value too long for '%(field)s' Skipping '%s': also in /etc/services Skipping '%s': couldn't process Skipping '%s': couldn't stat Skipping '%s': field too long Skipping '%s': invalid name Skipping '%s': name too long Skipping '%s': too big Skipping '%s': too many files read already Skipping IPv6 application rule. Need at least iptables 1.4 Skipping adding existing rule Skipping inserting existing rule Skipping malformed tuple (bad length): %s Skipping malformed tuple: %s Skipping unsupported IPv6 '%s' rule Status: active
%(log)s
%(pol)s
%(app)s%(status)s Status: active%s Status: inactive Title: %s
 To Unknown policy '%s' Unsupported action '%s' Unsupported policy '%s' Unsupported protocol '%s' Wrong number of arguments You need to be root to run this script n problem running running ufw-init uid is %(uid)s but '%(path)s' is owned by %(st_uid)s unknown y yes Project-Id-Version: ufw
Report-Msgid-Bugs-To: FULL NAME <EMAIL@ADDRESS>
POT-Creation-Date: 2009-08-25 10:20-0500
PO-Revision-Date: 2009-08-10 14:39+0000
Last-Translator: helix84 <Unknown>
Language-Team: Slovak <sk-i18n@lists.linux.sk>
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-Launchpad-Export-Date: 2009-08-26 05:56+0000
X-Generator: Launchpad (build Unknown)
 
Chyba pri pokuse použiť pravidlá aplikácie. 
Použitie: %(progname)s %(command)s

%(commands)s:
 %(enable)-31s zapína firewall
 %(disable)-31s vypína firewall
 %(default)-31s nastavuje štandardnú politiku
 %(logging)-31s nastavuje záznam na %(level)s
 %(allow)-31s pridáva povoliť %(rule)s
 %(deny)-31s pridáva zahodiť %(rule)s
 %(reject)-31s pridáva odmietnuť %(rule)s
 %(limit)-31s pridáva obmedziť %(rule)s
 %(delete)-31s zmaže %(urule)s
 %(insert)-31s vloží %(urule)s na pozíciu %(number)s
 %(status)-31s zobrazí stav firewallu
 %(statusnum)-31s zobrazí stav firewallu ako očíslovaný zoznam %(rules)s
 %(statusverbose)-31s zobrazí podrobne stav firewallu
 %(show)-31s zobrazí správu firewallu
 %(version)-31s zobrazí informácie o verzii

%(appcommands)s:
 %(applist)-31s list application profiles
 %(appinfo)-31s show information on %(profile)s
 %(appupdate)-31s update %(profile)s
 %(appdefault)-31s set default application policy
  (preskočené opätovné načítanie firewallu)  Požadované pravidlá úspešne zrušené.  Pokračovať v operácii (%(yes)s|%(no)s)?   Niektoré pravidlá nebolo možné zrušiť. %s je zapisovateľný pre skupinu! %s je zapisovateľný pre všetkých! „%(f)s“ súbor „%(name)s“ neexistuje „%s“ neexistuje (urobte príslušné aktualizácie vo vašich pravidlách) : Vyžaduje aspoň Python 2.5)
 Zrušené Operácia Pridanie pravidla IPv6 zlyhalo: IPv6 nie je zapnutý Dostupné aplikácie: Chybná cieľová adresa Chybný názov rozhrania Chybný názov rozhrania: nemožno použiť aliasy rozhrania Chybný typ rozhrania Chybný port Chybný port „%s” Chybná zdrojová adresa Nie je možné vložiť pravidlo na pozíciu „%d“ Nemožno vložiť pravidlo na pozíciu „%s“ Nemožno zadť „all“ spolu s „--add-new“ Nie je možné špecifikovať vkladanie a zmazanie Kontroluje sa ip6tables
 Kontroluje sa iptables
 Kontrolujú sa nespracované ip6tables
 Kontrolujú sa nespracované iptables
 Kontroly vypnuté Príkaz môže narušiť existujúce pripojenia ssh. Nepodarilo sa stiahnuť pravidlo „%s“ Nemožno zmazať neexistujúce pravidlo Nepodarilo sa násť profil zodpovedajúci „%s“ Nenájdený spustiteľný súbor „%s“ Nebolo možné násť profil „%s“ Nepodarilo sa nájsť protokol Nepodarilo sa načítať pravidlá zaznamenávania Nebolo možné normalizovať cieľovú adresu Nebolo možné normalizovať zdrojovú adresu Nebolo možné vykonať „%s“ Nepodarilo sa nastaviť LOGLEVEL Nebolo možné aktualizovať bežiaci firewall Nenájdený „%s“ Nenájdený PID rodiča „%s“ Nenájdený PID (je /proc pripojený?) Nebolo možné otvoriť „%s” na čítanie Nie je možné vykonať stat „%s” Nepodarilo sa aktualizovať pravidlá aplikácie Nebolo možné aktualizovať súbor pravidiel Štandardná politika aplikácie zmenená na „%s“ Popis: %s

 Duplicitný profil „%s“, používa sa posledné nájdené CHYBA: tento skript nemá byť SGID CHYBA: tento skript nemá byť SUID Firewall je aktívny a zapnutý pri štarte systému Firewall nie je zapnutý (preskakuje sa opätovné načítanie) Firewall znovu načítaný Firewall je zastavený a nespustí sa pri štarte systému Nájdená presná zhoda Nájdené viaceré zhody pre „%s“. Prosím, použite presný názov profilu Nájdená zhoda, ktorá nie je akcia ani typ záznamu  (%(xa)s/%(ya)s %(xl)s/%(yl)s) Od Podpora IPv6 nie je zapnutá Nesprávna syntax pravidla Neplatná syntax pravidla („%s“ uvedené s pravidlom aplikácie) Pozícia na vloženie „%s“ nie je platná pozícia Neplatná kluzula „from” Neplatná kluzula „port” Neplatná kluzula „proto” Neplatná kluzula „to” Neplatná verzia IP „%s” Neplatná klauzula rozhrania Neplatná úroveň zaznamenávania „%s“ Neplatný typ záznamu „%s“ Neplatná voľba Neplatné porty v profile „%s“ Neplatná pozícia ' Neplatná pozícia „%d“ Neplatný profil Neplatný názov profilu Neplatný token „%s” Záznam vypnutý Záznam zapnutý Zaznamenávanie:  Zmiešané verzie IP „od” a „do” Musíte uviesť „tcp“ alebo „udp“ s viacerými portami „%s“ vyžaduje „from“ alebo „to“ Vyžaduje sa klauzula „od” alebo „do” Nové profily: Bez zhody V profile „%s“ neboli nájdené žiadne porty Pre profil aplikácie neboli nájdené žiadne pravidlá Tu nie je voľba „log“ povolená Tu nie je voľba „log-all“ povolená Rozsahy portov musia byť číselné Port: Porty: Profil „%(fn)s“ nemá vyplnené povinné pole „%(f)s“ Profilu „%(fn)s“ chýba povinné pole „%(f)s“ Profil: %s
 Adresár profilov neexistuje Nezhoda protokolov (od/do) Nezhoda protokolov s určeným protokolom %s Pravidlo pridané Pravidlo sa zmenilo po normalizácii Pravidlo zmazané Pravidlo vložené Pravidlo aktualizované Pravidlá aktualizované Pravidlá aktualizované (v6) Pravidlá profilu „%s“ aktualizované Preskočené opätovné načítanie firewallu Preskakuje sa „%(value)s“: hodnota je príliš dlhá pre „%(field)s“ Preskakuje sa „%s“: tiež v /etc/services Preskakuje sa „%s“: nepodarilo sa spracovať Preskakuje sa „%s“: nepodarilo sa stat() Preskakuje sa „%s“: pole príliš dlhé Preskakuje sa „%s“: neplatný názov Preskakuje sa „%s“: názov príliš dlhý Preskakuje sa „%s“: príliš veľký Preskakuje sa „%s“: už bolo načítaných priveľa súborov Preskakuje sa aplikačné pravidlo IPv6. Vyžaduje iptables aspoň verzie 1.4 Preskakuje sa pridanie existujúceho pravidla Preskakuje sa vloženie existujúceho pravidla Preskakuje sa chybná n-tica (chybná dĺžka): %s Preskakuje sa chybná n-tica: %s Preskakuje sa nepodporované pravidlo IPv6 „%s” Statv: aktívny
%(log)s
%(pol)s
%(app)s%(status)s Stav: aktívny%s Stav: neaktívny Názov: %s
 Do Neznáma politika „%s“ Nepodaporovaná operácia „%s“ Nepodporovaná politika „%s” nepodporovaný protokol „%s” Chybný počet argumentov Aby ste mohli spúšťať tento skript, musíte byť root n problém pri spúšťaní spúšťa sa ufw-init uid je %(uid)s ale „%(path)s“ vlastní %(st_uid)s neznáme a ano 