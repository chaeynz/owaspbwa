<?php
  require_once('../genclude.php');
  require_once("statistics/utilities.php");

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=windows-1250">
  <link type="text/css" href="../style.css" rel="stylesheet">
  </head>
  <body  class="body">
  <div style="margin-left: 40px;">
    <?php
      
      if(isset($_GET['id']) && preg_match('/^[0-9]+$/', $_GET['id']) && fileExists($_GET['id'])){
      
        echo "<br/><a href='statistics.php'>BACK</a><br/><br/>";
                
        $scanEntries = fileToScan($_GET['id']);
        $descEntries = fileToDesc();
        
        ?>
        
        <table>
          <tr>
            <td>Coverage</td> 
            <td>:&nbsp;&nbsp;<b><span id="coverage">%<?php echo intval(100*count($scanEntries)/count($descEntries)); ?></span></b></td>
          </tr>
          <tr>
            <td>Started at</td> 
            <td>:&nbsp;&nbsp;<b><?php echo date('Y m d H:i:s', htmlentities($_GET['id'], ENT_QUOTES));?></b></td>
          </tr>
          <tr>
            <td>Details</td> 
            <td>:</td>
          </tr>
        </table>
        <br/>
        <span class="explanation">purple rows indicate missed cases, other rows indicate hit.</span>
        <br/><br/>
        <table class="list">
          <thead>
            <th>URI</th>
            <th>Description</th>
            <th>Number of Accesses</th>
            <th>IP Address</th>
            <th>User Agent</th>
            <!--
            <th>Time of First Access</th>
            <th>Time of Last Access</th>
            -->
          </thead>
          <tbody>
        <?php
        
          /*
           currenturlsvisited's structure
            every element's key is REQUEST_URI and value is array X
            there are one or elements in value array X;
              
              key: noofaccess
              value: how many times this link is fetched in this session
              
              key: timefirstaccess
              value: the time the link is fetched
        
              key: timelastaccess
              value: the time the link is fetched
          */   
          $i = 0;
          foreach ($scanEntries as $k=>$v) {
            if($i%2 == 1) $cls = "odd";
            else  $cls = "even";
            
            echo "<tr class='" . $cls . "'>";
              echo "<td>" . $k . "</td>";
              echo "<td>" . $descEntries[$k] . "</td>";              
              echo "<td>" . $v["noofaccess"] . "</td>";
              echo "<td>" . $v["ipaddress"] . "</td>";
              if(strlen($v["useragent"]) < 70)
                echo "<td>" . htmlentities($v["useragent"], ENT_QUOTES) . "</td>";                
              else
                echo "<td>" . substr($v["useragent"], 0, 25) . " ... " . substr($v["useragent"], strlen($v["useragent"]) - 25, 25) . "</td>";
              //echo "<td>" . date('Y m d H:i:s', $v["timefirstaccess"]) . "</td>";
              //echo "<td>" . date('Y m d H:i:s', $v["timelastaccess"]) . "</td>";
            echo "</tr>";        
            $i++;  
          }
          
          // list missed urls
          foreach ($descEntries as $k=>$v){
            if(!array_key_exists($k, $scanEntries)){
              echo "<tr class='miss'>";
                echo "<td>" . $k . "</td>";
                echo "<td>" . $descEntries[$k] . "</td>";              
                echo "<td>N/A</td>";
                echo "<td>N/A</td>";
                echo "<td>N/A</td>";
                //echo "<td>" . date('Y m d H:i:s', $v["timefirstaccess"]) . "</td>";
                //echo "<td>" . date('Y m d H:i:s', $v["timelastaccess"]) . "</td>";
              echo "</tr>";        
            }
          }
          
        ?> 
          </tbody>
        </table>
        <br/>
        <br/>
            
        <?php
      }
      else{  
        $fileNames = listScanFiles();
        $bots = botsFileToArray();
            
        echo "<br/><br/>";
        rsort($fileNames);
        //$brCount = 0;
        foreach($fileNames as $aFileName){
          $tokens = explode(".", $aFileName);

          // get a user agent, maybe I shouldn't store user agent in values
          $scanEntries = fileToScan($tokens[0]);
          $values = array_values($scanEntries);
            
          // is this scan performed by a bot listed in our list
          $isBot = false;          
          foreach($bots as $aBot){
              if( stripos($values[0]["useragent"], trim($aBot)) !== false){
                $isBot = true;
                break;
              }
          }
          
          // if this wasn't a bot then list it to the user
          if(!$isBot){                        

             if(strlen($values[0]["useragent"]) < 70)
                $uagent = $values[0]["useragent"];                
              else
                $uagent = substr($values[0]["useragent"], 0, 25) . " ... " . 
                          substr($values[0]["useragent"], strlen($values[0]["useragent"]) - 25, 25);


              echo "<a href='statistics.php?id=" . 
                      $tokens[0] ."'>". date('Y m d H:i:s', $tokens[0]) . "</a> &nbsp;&nbsp;" . htmlentities($uagent, ENT_QUOTES);
              //$brCount++;
              //if($brCount%4 == 0)
              echo "<br/>";
          }
          
        }
        
        if(count($fileNames) == 0){
            echo "No statistics collected for now ...";
        }
      }
      ?>
  </div>
  </body>
</html>
