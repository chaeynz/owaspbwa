<?php

  // returns the file names in a directory
  function listPages(){
    $files = array();
    $items = scandir($_SESSION['pagesdir']);
    foreach($items as $k=>$v){
      if(is_file($_SESSION['pagesdir'].$v) && preg_match('/^.*\.php/', $v))
        array_push($files, $v);
    }
    return $files;
  }

  // returns the file names in a directory
  function listScanFiles(){
    $files = array();
    $items = scandir($_SESSION['statisticsdir']);
    foreach($items as $k=>$v){
      if(is_file($_SESSION['statisticsdir'].$v) && preg_match('/^.*\.dat/', $v))
        array_push($files, $v);
    }
    return $files;
  }
  
  function fileExists($filename){
    return is_file($_SESSION['statisticsdir'] . $filename . ".dat");
  }
  
  // saves a scan to a file
  function scanToFile($filename, $scan){
    /*
      $scan is an array. it's keys are URIs and values are arrays 
      in those arrays, individual items are stored as key/value pairs       
    */
      
    $handle = fopen($_SESSION['statisticsdir'] . $filename . ".dat", 'w');
    if($handle){
      foreach($scan as $k=>$v){
        fwrite($handle, $k);
        fwrite($handle, "###");
        fwrite($handle, $v["noofaccess"]);
        fwrite($handle, "###");
        fwrite($handle, $v["ipaddress"]);
        fwrite($handle, "###");
        fwrite($handle, $v["useragent"]);
        fwrite($handle, "###");
        fwrite($handle, $v["timefirstaccess"]);
        fwrite($handle, "###");
        fwrite($handle, $v["timelastaccess"]);
        fwrite($handle, "\n");           
      }    
      fflush($handle);
      fclose($handle);
    }
    else
      echo "cant open file : " . $_SESSION['statisticsdir'] . $filename . ".dat";    
  }
  
  // loads a file to a scan 
  function fileToScan($filename){
    $scan = array();
    
    $lines = file($_SESSION['statisticsdir'] . $filename . ".dat");
    foreach ($lines as $k=>$v){
      $tokens = explode("###", $v);
      $scan[$tokens[0]] = array("noofaccess" => $tokens[1],
                                "ipaddress" => $tokens[2],              
                                "useragent" => htmlentities($tokens[3], ENT_QUOTES), // you gotta be kiddin me.
                                "timefirstaccess" => $tokens[4],
                                "timelastaccess" => $tokens[5],                                                                                                
                                );
    }
    
    return $scan;
    
  }

  // loads a file to a key=>value array, which explains type of URLs found/unfound
  function fileToDesc(){
    $desc = array();
    
    $lines = file($_SESSION['statisticsdir'] . "links.txt");
    foreach ($lines as $k=>$v){
      $tokens = explode("#", $v);
      $desc[$tokens[0]] = $tokens[1];
    }
    
    return $desc;
    
  }
  
    //loads possible bots into an array. Requests coming from these bots will be logged but not shown
    // in the statistics page
    function botsFileToArray(){
        $desc = array();
        $lines = file($_SESSION['statisticsdir'] . "bots.txt");
        return $lines;
    }


  
?>
