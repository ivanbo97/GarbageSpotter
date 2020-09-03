<?php

// Route URL paths
if ($request->get('garbage')){
	$response->allgarbage = $db->querybind_all('SELECT * FROM garbage');
	echo $response->render(); 
}
if($request->get('persons')) {
	$response->persons = $db->querybind_all('SELECT a.id,a.name,a.age,c.name as country FROM authors a, countries c WHERE a.country_id = c.id');
}
else if($request->get('persons/[0-9]+')) {
	$person_id = (int) $request->segment(1);
	$response->person = $db->querybind_one('SELECT * FROM authors WHERE id = ?', [ $person_id ]);
	if(!$response->person) {
		$response->code(404);
		$response->error('404: Person Not Found.');
	}
}
else if($request->post('persons/[0-9]+') || $request->post('persons')) {
	$person_id = (int) $request->segment(1, 0);
	$person = $request->data;
	if($person) {	
		if(strlen($person->name) < 1) $response->error('Name is empty.');
		if(strlen($person->age) < 1) $response->error('Age is empty.');
	}
	else {
		$response->error('No JSON data sent.');
	}
	
	if($response->hasErrors()) {
		$response->code(400);
		$response->error('400: Invalid input.');
	}
	else {
		if($person_id > 0) { // update existing
			$result = $db->querybind(
				'UPDATE authors SET name=?, age=?, country_id=? WHERE id=?', 
				[$person->name, $person->age,$person->teltype_id, $person_id]
			);
		} else { // insert new
			$result = $db->querybind(
				'INSERT INTO persons SET fname=?, lname=?, address=?', 
				[$person->fname, $person->lname, $person->address]
			);
			$person_id = $db->insert_id;
		}
		
		$response->person = $db->querybind_one('SELECT * FROM authors WHERE id = ?', [$person_id]);
		$response->info('Person saved.');	
	}
}
else if($request->delete('persons/[0-9]+')) {
	$person_id = (int) $request->segment(1);
	$db->querybind('DELETE FROM books WHERE author_id = ?', [$person_id] );
	$db->querybind('DELETE FROM authors WHERE id = ?', [$person_id] );
	$response->info("Person id=$person_id and its telephones deleted.");
}
else if($request->get('persons/[0-9]+/telephones')) {
	$person_id = (int) $request->segment(1);
	$response->person = $db->querybind_one('SELECT * FROM authors WHERE id = ?', [$person_id] );
	$response->telephones = [];
	if($response->person) {
		$response->telephones = $db->querybind_all('SELECT * FROM books WHERE author_id = ?', [$person_id] );
	}
	else {
		$response->code(404);
		$response->error("404: Person id=$person_id not found.");
	}
}
else if($request->get('telephones/[0-9]+')) {
	$telephone_id = (int) $request->segment(1);
	$response->telephone = $db->querybind_one('SELECT * FROM telephones WHERE id = ?', [ $telephone_id ]);
	if(!$response->telephone) {
		$response->code(404);
		$response->error('404: Telephone Not Found.');
	}
}
else if($request->post('telephones/[0-9]+') || $request->post('telephones')) {
	$telephone_id = (int) $request->segment(1);
	$telephone = $request->data; // deserialized JSON object sent over the network.
	if($telephone) {
		if(strlen($telephone->number) < 1) $response->error('Number is empty.');
		if($telephone->person_id < 1) $response->error('Missing person_id.');
		if($telephone->teltype_id < 1) $response->error('Type is empty.');
//		$teltype = $db->querybind_one("SELECT * FROM teltypes WHERE id = ?", [$telephone->teltype_id + 0]);
//		if(!$teltype) $response->error('Type is invalid.');
	}
	else {
		$response->error('No JSON data sent.');
	}
	
	if($response->hasErrors()) {
		$response->code(400);
		$response->error('400: Invalid input.');		
	}
	else {
		$args = [$telephone->person_id, $telephone->teltype_id, $telephone->number, $telephone_id];
		
		if($telephone_id > 0) { // update existing
			$result = $db->querybind('UPDATE telephones SET person_id=?, teltype_id=?, number=? WHERE id=?', $args);
		} else { // insert new
			$result = $db->querybind('INSERT INTO telephones SET person_id=?, teltype_id=?, number=?', $args);
			$telephone_id = $db->insert_id;
		}

		$response->telephone = $db->querybind_one('SELECT * FROM telephones WHERE id = ?', [$telephone_id]);
		$response->info('Telephone saved.');	
	}
}
else if($request->delete('telephones/[0-9]+')) {
	$telephone_id = (int) $request->segment(1);
	$db->querybind('DELETE FROM books WHERE id = ?', [$telephone_id] );
	$response->info("Telephone id=$telephone_id deleted.");
}
else if($request->get('teltypes')) {
	$response->teltypes = $db->querybind_all('SELECT * FROM countries ORDER BY id');
}
else {
	$response->error('404: URL Not Found: /'.$request->path);
	$response->code(404);
}

// Outputs $response object as JSON to the client.
echo $response->render();