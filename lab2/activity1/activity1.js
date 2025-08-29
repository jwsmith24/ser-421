// activity1.js
// DOM manipulation for Lab 2 activity 1
// Jacob Smith (jwsmit24)


// 1.1: Change the "More Results" text in the button at the bottom of the search page
// to "More redundant Results!"
document.getElementById("more-results").textContent = "More redundant Results!";

// 1.2: Output the number of <li> elements on the page to the console
// returns a node list of every element that matches the query that has a length prop
console.log(document.querySelectorAll("li").length);


// 1.3: Get the search bar value by using the id
// id = search_form_input
console.log(document.getElementById("search_form_input").value);

// 1.4: Remove the duck logo in the upper left corner disappear
// id = header-logo-wrapper
document.getElementById("header-logo-wrapper").remove();
