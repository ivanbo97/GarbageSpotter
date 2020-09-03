function render_garbage (data){
	
	for(var i=0; i<data.length; i++){
		
		var single_garbage = data[i];
		var icon_url = "<img src= images/" + single_garbage.image + " alt= yamaha logo width= 400 height= 600/>"+" Latitude: " +single_garbage.lat +
		"\n Longitude: " + single_garbage.lon;

        L.marker([single_garbage.lat, single_garbage.lon], {icon: greenIcon}).addTo(map).bindPopup(icon_url);
		
	}
}
	
function html_escape(val) {
	return (val+'')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/\"/g, '&quot;')
      .replace(/\'/g, '&apos;');
}

