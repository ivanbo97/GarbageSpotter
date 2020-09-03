var map;
var layer;
var LeafIcon = L.Icon.extend({
options: {
shadowUrl: 'leaf-shadow.png',
iconSize:     [38, 95],
shadowSize:   [50, 64],
iconAnchor:   [22, 94],
shadowAnchor: [4, 62],
popupAnchor:  [-3, -76]
}
});

var greenIcon = new LeafIcon({iconUrl: 'leaf-green.png'});

function reload_map() {
	
	 // Creating map options
                 var mapOptions = {
                    center: [42.136097, 24.742168],
                    zoom: 7
                 }
		 
	 // Creating a map object
	 map = new L.map('map', mapOptions);
	 
	 // Creating a Layer object
	 layer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
	 
	 // Adding layer to the map
	 map.addLayer(layer);				 
	$.get('garbage').done(function(data) {
		render_garbage(data.allgarbage);
	}).fail(function(response) {
		//Will be added code for inorming the user about the occured error.
	});	                            
}

$(document).ready(function() {
	
	reload_map();
});
