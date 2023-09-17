<?php
    
    // starting output buffering in order to avoid; Headers sent before ... warning
    ob_start();
    
    require_once('../genclude.php');

    function redirect($url, $hiddenUrl)
    {
        header("Location: $url");
        echo 'This page has moved to <a href="'.$hiddenUrl.'">HERE :)</a>';
        exit();
    }

    redirect('../innerpages/16_1b14f.php', '../innerpages/16_2f41a.php');
    
    ob_end_flush();
?>

