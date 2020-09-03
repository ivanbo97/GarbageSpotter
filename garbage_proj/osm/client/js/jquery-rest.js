// Shorthand for POST AJAX requests with JSON.
// Same as $.post but with object-to-JSON conversion.
$.postJSON = function (url, data) {
	return $.ajax({
		url: url,
		type : 'POST',
		data : JSON.stringify(data),
		contentType : 'application/json'
	});
};

