<?php
  require_once('genclude.php');
  require_once('offscanpages/statistics/utilities.php');
  
  $menuPages = listPages();
  sort($menuPages, SORT_NUMERIC);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="content-type" content="text/html; charset=windows-1250">
  <link type="text/css" href="style.css" rel="stylesheet"/>
  <base href="<?php echo $_SESSION['baseaddr']; ?>pages/" target="body">
  </head>
  <body class="menu">
    <div class="menulinks">
      <?php
        
        foreach ($menuPages as $k=>$v){
          if(strcmp($v,"100.php") != 0)
            echo "<div class='menulink'><a href='".$v."'>".$v."</a></div>";
        }
      ?>      
      <div class="menulink">&nbsp;</div>
      <div class="menulink">&nbsp;</div>
      <div class="menulink"><a href="<?php echo $_SESSION['baseaddr']; ?>offscanpages/statistics.php" target="body">Statistics</a></div>
      <div class="menulink"><a href="<?php echo $_SESSION['baseaddr']; ?>offscanpages/current.php">Current Run</a></div>
      <div class="menulink"><a href="<?php echo $_SESSION['baseaddr']; ?>offscanpages/about.php">About</a></div>
      <div class="menulink"><a href="100.php">Logout</a></div>
    </div>
  </body>
</html>
