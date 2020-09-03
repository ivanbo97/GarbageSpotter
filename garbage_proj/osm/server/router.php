<?php

// Route URL path
if ($request->get('garbage')){
	$response->allgarbage = $db->querybind_all('SELECT * FROM garbage');
	echo $response->render(); 
}
?>
