
$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});

	getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;

	if (catIdField.length) {
		editMode = true;
	}

	if (!editMode) getCategories();
}

function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}
$("#fileImage").change(function() {
	if (!checkFileSize(this)) {
		return;
	}
	showImageThumbnail(this);
}
);

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}
function checkFileSize(fileInput) {
	fileSize = fileInput.files[0].size;
	if (fileSize > 1048576) {
		fileInput.setCustomValidity("You must choose an image less than 1MB!");
		fileInput.reportCalidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}

}