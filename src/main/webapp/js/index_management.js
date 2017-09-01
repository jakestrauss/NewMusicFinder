/**
 * This js script manages the form on the index.jsp page
 */

//ensure no more than 5 boxes can be checked at once
var limit = 5;
$("input[name='genres']").on('change', function (evt) {
      if($("input[name='genres']:checked").length > limit) {
       this.checked = false;
   		}
  });


//makes sure at least one genre is checked
$('button[type="submit"]').on('click', function() {
	$cbx_group = $("input:checkbox[name='genres']");

	$cbx_group.prop('required', true);
	if($cbx_group.is(":checked")){
	  $cbx_group.prop('required', false);
	}
});

//change html validation message
document.addEventListener("DOMContentLoaded", function() {
    var elements = document.getElementsByClassName("custom-required");
    for (var i = 0; i < elements.length; i++) {
        elements[i].oninvalid = function(e) {
            e.target.setCustomValidity("");
            if (!e.target.validity.valid) {
                e.target.setCustomValidity("Please select at least one genre");
            }
        };
        elements[i].oninput = function(e) {
            e.target.setCustomValidity("");
        };
    }
})
