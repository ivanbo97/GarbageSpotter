
<?php 

	$file_path = "recordings/";

	$file_path = $file_path.basename($_FILES['uploaded_file']['name']);

	if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'],$file_path))	
	 echo "success";
	else 
	 echo "error";

	$db = new Mysqli('127.0.0.1:3306', 'root', '', 'garbage');

	set_time_limit(1000);

	$iterator = simplexml_load_file($file_path,"SimpleXMLIterator");

	$wpt = $iterator->wpt;
	$lat = $wpt["lat"];
	$lon = $wpt ["lon"];
	$image = $wpt->extensions->img->__toString();

	$db->query("INSERT INTO garbage SET lat='$lat',lon='$lon',image='$image'");

	unset($iterator);
	echo "success";
	http_response_code(200);
?>