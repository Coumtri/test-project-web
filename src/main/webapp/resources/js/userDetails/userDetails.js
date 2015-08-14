$(document).ready(function() {
    alert("hello julien !!");

    doAjaxPost();
};

// http://crunchify.com/how-to-use-ajax-jquery-in-spring-web-mvc-jsp-example/
// http://www.javacodegeeks.com/2012/02/ajax-with-spring-mvc-3-using.html
        function doAjaxPost() {
        $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/showUserDetails",
                success: function(html) {
                    $("#ajax-content").empty().append(html);
                },
                error: function(e){
                        alert('Error: ' + e);
                        }
        });
        }

// Source : http://stackoverflow.com/questions/907634/is-this-how-you-define-a-function-in-jquery
var extractTable = function(responseParam)
{
        var response = $.parseJSON($responseParam);
        $.each($response, function(i, item) {
            var $tr = $('<tr>').append(
                $('<td>').text(item.rank),
                $('<td>').text(item.content),
                $('<td>').text(item.UID)
            ); //.appendTo('#records_table');
            console.log($tr.wrap('<p>').html());
        });
};
