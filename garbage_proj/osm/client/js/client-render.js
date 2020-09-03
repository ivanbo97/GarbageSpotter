// All these function render piece of HTML to plug into the DOM tree.
// The HTML can be plugged using $('#id').html(new_html);


function render_garbage (data){
	


	for(var i=0; i<data.length; i++){
		
		var single_garbage = data[i];
		var icon_url = "<img src= images/" + single_garbage.image + " alt= yamaha logo width= 400 height= 600/>"+" Latitude: " +single_garbage.lat +
		"\n Longitude: " + single_garbage.lon;

        L.marker([single_garbage.lat, single_garbage.lon], {icon: greenIcon}).addTo(map).bindPopup(icon_url);
		
	}
}
function render_authors(authors) {
	var html = "<tr>"+
			"<th>ID</th>"+
			"<th>Name</th>"+
			"<th>Age</th>"+
			"<th>Country</th>"+
			"<th></th>"+
		"</tr>";

	for(var i=0; i<authors.length; i++) {
		var p = authors[i];
		html += "<tr>" +
			"<td>" + p.id + "</td>" +
			"<td><a href='#' data-person-id='" + p.id + "' class='person-telephones'>" +
				html_escape(p.name) +
			"</a></td>"+
			"<td>" + html_escape(p.age) + "</td>" +
			"<td>" + html_escape(p.country) + "</td>" +
			"<td>" +
				"<a href='#' data-person-id='" + p.id + "' class='edit_icon person-edit'>Edit</a> " +
				"<a href='#' data-person-id='" + p.id + "' class='delete_icon person-delete'>Delete</a>" +
			"</td>" +
		"</tr>";
	}

	html = "<table class='grid'>"+html+"</table>";
	return html;
}

function render_person_form(person) {
	if(!person) return 'Empty person.';
	
	var html = '';
	var title = (person.id) ? 'Edit Author' : 'Add Author';
	
	html += "<h1>" + title + "</h1>";
	html += "<form action='#' method='post'>";
	html += "<p><label>ID</label><input name='id' value='" + html_escape(person.id) + "' readonly='readonly' /></p>";
	html += "<p><label>Name</label><input name='name' value='" + html_escape(person.name) + "'/></p>";
	html += "<p><label>Age</label><input name='age' value='" + html_escape(person.age) + "'/></p>";
	html += "<p><label>Country ID</label><input name='countryid' value='" + html_escape(person.country_id) + "'readonly='readonly'/></p>";
	html += "<p><button>Save</button></p>";
	html += "</form>";
	
	return html;
}

// TELEPHONES
function render_telephones(author, books) {	
	var html = '';
	
	html += "<p class='user_icon'>"+
			"<b>" + html_escape(author.name) + "</b>, Number of books:  "+ 
			html_escape(books.length) + 
		"</p>";
	
	html += "<table class='grid'>";
	html += "<tr>"+
		"<th>ID</td>"+
		"<th>Title</th>"+
		"<th>Pages</th>"+
		"<th></th>"+
	"</tr>";
	for(var i=0; i<books.length; i++) {
		var book = books[i];
		//var teltype = get_teltype(tel.teltype_id);
		html += "<tr>"+
			"<td>" + book.id + "</td>" +
			"<td>" + book.title + "</td>" +
			"<td>" + html_escape(book.pages) + "</td>" +
			"<td>" +
				"<a href='#' data-person-id='" + author.id + "' data-telephone-id='" + book.id + "' class='edit_icon telephone-edit'>Edit</a> " +
				"<a href='#' data-person-id='" + author.id + "' data-telephone-id='" + book.id + "' class='delete_icon telephone-delete'>Delete</a>" +
			"</td>"+
		"</tr>";
	}
	html += "</table>";
	
	html += "<p>" +
		"<a href='#' data-person-id='" + author.id + "' class='add_icon telephone-add'>Add New Book</a> " +
		"<a href='#' data-person-id='" + author.id + "' class='refresh_icon telephones-refresh'>Refresh</a>" +
		"</p>";

	return html;
}

function render_telephone_form(book) {
	if(!book) return 'Empty telephone.';
	
	var html = '';
	var title = (book.id) ? 'Edit Book' : 'Add Book';
	
	html += "<h1>" + title + "</h1>";
	html += "<form action='#' method='post'>";
	html += "<p><label>ID</label><input name='id' value='" + html_escape(book.id) + "' readonly='readonly' /></p>";
	html += "<p><label>AUTHOR_ID</label><input name='author_id' value='" + html_escape(book.author_id) + "' readonly='readonly' /></p>";
	html += "<p><label>TITLE</label><input name='title' value='" + html_escape(book.title) + "'/></p>";
	html += "<p><label>PAGES</label><input name='pages' value='" + html_escape(book.pages) + "'/></p>";
	html += "<p><label>YEAR</label><input name='year' value='" + html_escape(book.year) + "'/></p>";
	

	html += "<p><button>Save</button></p>";
	html += "</form>";
	
	return html;
}

function render_messages(messages) {
	var html = '';
	if(messages) {	
		for(var i = 0; i < messages.length; i++) {
			var m = messages[i];
			var css = (m.type === 'error') ? 'error_icon' : 'info_icon';
			html += "<p class='" + css + "'>" + m.text + "</p>";
		}
	}
	return html;
}

function get_teltype(teltype_id) {
	// TELTYPES is global variable preloaded on client start.
	for(var i=0; i < TELTYPES.length; i++) {
		if(TELTYPES[i].id == teltype_id) {
			return TELTYPES[i];
		}
	}
	return null;
}
	
function html_escape(val) {
	return (val+'')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/\"/g, '&quot;')
      .replace(/\'/g, '&apos;');
}

