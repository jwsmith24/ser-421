import {dictionary} from "./eliza-dictionary"; // pull in the eliza dictionary

// user name
const elizaDisplay = document.getElementById("elizaMessage");
// input field
const userInput = document.getElementById("userInput");


// flag to track if we've moved past the greeting
let greeted = false;


// randomly choose from possible greeting templates:
const getGreeting = (name) => {
    const options = [
        `Hi, ${name}! How are you?`,
        `Hey ${name}, how is your day going?`,
        `It seems like something is troubling you, ${name}. Want to talk about it?`,
        `Hello ${name}, and welcome to another glorious conversation with a computer.`
    ]
    const randomIndex = Math.floor(Math.random() * options.length);
    greeted = true;
    return options[randomIndex];
}


const getResponse = (keywords) => {

}


// form entry event listener
const entryButton = document.getElementById("entryButton");
entryButton.addEventListener("click", (event) => {
    event.preventDefault(); // stop the form from submitting and refreshing the page
    greeted ? handleConversation() : handleGreeting();
})

const handleGreeting = () => {
    elizaDisplay.textContent = getGreeting(userInput.value);
    userInput.value = "";
}

const handleConversation = () => {
    // break up text from user into words (array of strings)
    // for each word, search for a matching keyword in the dictionary
    // use first match for simplicity
    // select a random option from the associated keyword
}