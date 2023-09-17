<?php
  require_once('../genclude.php');
  require_once("statistics/utilities.php");
  $descEntries = fileToDesc();

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=windows-1250">
  <link type="text/css" href="../style.css" rel="stylesheet">
  </head>
  <body  class="body">
    <div style="margin-left: 40px; padding-bottom: 40px;">
      
      <table>
        <tr>
          <td>Coverage</td>
          <td>:&nbsp;&nbsp;<b><span id="coverage">%<?php echo intval(100*count($_SESSION['currenturlsvisited'])/count($descEntries)); ?></span></b></td>
        </tr>
        <tr>
          <td>Started at</td> 
          <td>:&nbsp;&nbsp;<b><?php echo date('Y m d H:i:s', $_SESSION['time']);?></b></td>
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
        foreach ($_SESSION['currenturlsvisited'] as $k=>$v) {
          if($i%2 == 1) $cls = "odd";
          else  $cls = "even";
          
          echo "<tr class='" . $cls . "'>";
            echo "<td>" . $k . "</td>";
            echo "<td>" . $descEntries[$k] . "</td>";              
            echo "<td>" . $v["noofaccess"] . "</td>";
            
            if(strlen($v["useragent"]) < 70)
              $uagent = $v["useragent"];                
            else
              $uagent = substr($v["useragent"], 0, 25) . " ... " . 
                        substr($v["useragent"], strlen($v["useragent"]) - 25, 25);
  
            
            echo "<td>" . $v["ipaddress"] . "</td>";
            echo "<td>" . htmlentities($uagent, ENT_QUOTES) . "</td>";
            //echo "<td>" . date('Y m d H:i:s', $v["timefirstaccess"]) . "</td>";
            //echo "<td>" . date('Y m d H:i:s', $v["timelastaccess"]) . "</td>";
          echo "</tr>";        
          $i++;  
        }
        
        // list missed urls
        foreach ($descEntries as $k=>$v){
          if(!array_key_exists($k, $_SESSION['currenturlsvisited'])){
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
      <table>
    </div>     
  </body>
</html>
