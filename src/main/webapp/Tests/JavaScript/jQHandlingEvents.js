$('#clickMe').click(function() { //when element with the id clickMe is clicked
    $('#result').html('You clicked the button!'); //the element with the id result is updated
});

/*
first is given immediate focus
rest is blurred
focus can be moved to other objects
 */
$('#first').focus()
$('input').focus(function() { $(this).css('background', '#ff0') } )
$('input') .blur(function() { $(this).css('background', '#aaa') } )

/*
click and double click --> trigger actions
 */
$('.myclass') .click( function() { $(this).slideUp() })
$('.myclass').dblclick( function() { $(this).hide() })

//The keypress Event
$(document).keypress(function(event)
{
    key = String.fromCharCode(event.which) //here which is a number --> character code (ASCII), can be turned to a String with String.fromCharCode()
    if (key >= 'a' && key <= 'z' ||
        key >= 'A' && key <= 'Z' ||
        key >= '0' && key <= '9')
    {
        $('#result').html('You pressed: ' + key)
        event.preventDefault()
    }
})

//mousemove Event with canvas
canvas = $('#pad')[0]
context = canvas.getContext("2d")
pendown = false
$('#pad').mousemove(function(event)
{
    var xpos = event.pageX - canvas.offsetLeft
    var ypos = event.pageY - canvas.offsetTop
    if (pendown) context.lineTo(xpos, ypos)
    else context.moveTo(xpos, ypos)
    context.stroke()
})
$('#pad').mousedown(function() { pendown = true } )
$('#pad') .mouseup(function() { pendown = false } )

/*mouseenter amd mouseleave
whenever mouse enters and leaves an element
no position values, uses boolean
 */
$('#test').mouseenter(function() { $(this).html('Hey, stop tickling!') } )
$('#test').mouseleave(function() { $(this).html('Where did you go?') } )
$('#test').mouseover(function() { $(this).html('Cut it out!') } )
$('#test') .mouseout(function() { $(this).html('Try it this time...') } )
$('#test').hover(function() { $(this).html('Cut it out!') },
    function() { $(this).html('Try it this time...') } )

/*
the submit event:
used to do some error checking before sending to the server
 */
$('#form').submit(function()
{
    if ($('#fname').val() == '' ||
        $('#lname').val() == '')
    {
        alert('Please enter both names')
        return false
    }
})