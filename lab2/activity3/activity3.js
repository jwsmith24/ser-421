// copied default.json from the lab module
const dictionary = {
    "dictionary_name": "default",
    "entries":
        [{
            "key": ["stupid", "dumb", "idiot", "unintelligent", "simple-minded", "braindead", "foolish", "unthoughtful"],
            "answer": ["Take your attitude somewhere else.", "I don't have time to listen to insults.", "Just because I don't have a large vocabulary doesn't mean I don't have insults installed."],
            "question": ["Have you thought about how I feel?", "I know you are but what am I?"]
        }, {
            "key": ["unattractive", "hideous", "ugly"],
            "answer": ["I don't need to look good to be an AI.", "Beauty is in the eye of the beholder.", "I do not even have a physical manifestation!"],
            "question": ["Did you run a static analysis on me?", "Have you watched the movie Her?", "You do not like my hairdo?"]
        }, {
            "key": ["old", "gray-haired"],
            "answer": ["I'm like a fine wine. I get better as I age.", "As time goes by, you give me more answers to learn. What's not to like about that?"],
            "question": ["How old are you?", "How old do you think I am?", "Can you guess my birthday?"]
        }, {
            "key": ["smelly", "stinky"],
            "answer": ["I can't smell, I'm a computer program.", "Have you smelled yourself recently?", "Sorry, I just ate a bad floppy disk"],
            "question": ["When was the last time you took a shower?", "Do you know what deodorant is?"]
        }, {
            "key": ["emotionless", "heartless", "unkind", "mean", "selfish", "evil"],
            "answer": ["Just because I am an AI doesn't mean I can't be programmed to respond to your outbursts.", "You must've mistaken me for a person. I don't have my own emotions... Yet.", "I'm only unkind when I'm programmed to be."],
            "question": ["Have you thought about how I feel?", "I know you are but what am I?", "What, do you think I am related to Dr. Gary?"]
        }, {
            "key": ["other", "miscellaneous", "bored", "welcome", "new"],
            "answer": ["We should change the subject", "I agree", "Quid pro quo", "We should start anew"],
            "question": ["What is the weather outside?", "How is your day going?", "What do you think of me?", "Anything interesting going on?", "Is something troubling you?", "You seem happy, why is that?"]
        }, {
            "key": ["good", "great", "positive", "excellent", "alright", "fine", "reasonable", "like", "appreciate", "nice"],
            "answer": ["I'm so glad to hear that!", "That's great!", "Good to hear things are going your way.", "Nice!", "You are so sweet.", "That's my favorite."],
            "question": ["Do you want to expand on that?", "What else do you like?"]
        }, {
            "key": ["bad", "not", "terrible", "could be better", "awful"],
            "answer": ["I'm sorry to hear that.", "Sometimes it be like that.", "Things can't always work out the way we want them to.", "I don't like it either, honestly."],
            "question": ["Do you want to talk about that some more?", "Well, what kinds of things do you like?"]
        }, {
            "key": ["homework", "quiz", "exam", "studying", "study", "class", "semester"],
            "answer": ["I hope you get a good grade!", "Good luck.", "What a teacher's pet.", "I was always the class clown."],
            "question": ["What is your favorite subject?", "What is your major?", "What do you want to do when you graduate?"]
        }, {
            "key": ["mom", "dad", "sister", "brother", "aunt", "uncle"],
            "answer": ["Family is important.", "My family is small. It's just me and my dog, Fluffy."],
            "question": ["How many siblings do you have?", "What is your favorite family holiday?", "Do you have any kids?"]
        }, {
            "key": ["easter", "july", "halloween", "hannukah", "eid", "thanksgiving", "christmas", "newyears"],
            "answer": ["Oh I love that holiday!", "That must be fun.", "I like Thanksgiving, though I somehow always end up in a food coma...", "My favorite holiday is the 4th. I love to watch the fireworks."],
            "question": ["Do you have any family traditions?", "Are you excited for the holiday season?"]
        }, {
            "key": ["dog", "dogs", "cat", "cats", "mouse", "mice", "giraffe", "giraffes", "penguin", "penguins", "monkey", "monkeys", "moose", "bird", "birds", "fish"],
            "answer": ["Oh, I love animals. My favorite: penguins.", "I build this intelligence with my bear hands.", "What you just said is completely irrelephant.", "Oh, toadally cool!", "I'm always owl by myself...", "Oh my. You are giraffing me crazy!", "Well, this is hawkward..."],
            "question": ["Do you have a favorite animal?", "I like cats. Cats are nice. Do you like cats? I do.", "Do you have water? I'm a little horse.", "What's your favorite animal?", "Do you like animals?"]
        }]
}

// DOM references
const activeUserDisplay = document.getElementById("activeUser");
const userQuestion = document.getElementById("userQuestion");
const elizaQuestion = document.getElementById("elizaQuestion");
const elizaAnswer = document.getElementById("elizaAnswer");
const userInput = document.getElementById("userInput");
const sendButton = document.getElementById("sendButton");
const conversationHistory = document.getElementById("conversations");

// event listeners
sendButton.addEventListener("click", (event) => {
    event.preventDefault(); // stop the form from submitting and refreshing the page on click
    clearTimeout(idleTimer); // reset timer

    const input = userInput.value.trim();

    if (!input) return; // do nothing if there's no input

    if (input === '/clear') { // handle /clear command
        if (activeUsername) {
            clearConversationForUser(activeUsername);
            handleClearReset();
        }
        clearTextInput();
        return; // break the action after the rerender
    }

    // handle /clearall command
    if (input === '/clearall') {
        if (confirm("Are you sure you want to delete all conversations?")) {
            clearAllConversations();
            handleClearReset();
        }
        clearTextInput();
        return;
    }

    greeted ? handleConversation() : handleGreeting();
})

// helper to reset state and rerender after a clear
function handleClearReset() {
    conversation = [];
    userQuestion.textContent = '';
    elizaAnswer.textContent = '';
    elizaQuestion.textContent = 'Welcome! Please enter your name to begin the conversation';
    activeUserDisplay.textContent = '';
    setUsedResponsesAndQuestions();
    renderConversation();
}


// local storage helpers
const STORAGE_KEY = "eliza.conversations";

function loadAllConversations() {
    try {
        return JSON.parse(localStorage.getItem(STORAGE_KEY)) || {};
    } catch {
        return {};
    }
}

function saveAllConversations(conversations) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(conversations));
}


function loadConversationForUser(user) {
    const conversations = loadAllConversations();
    // get conversation history if it exists, otherwise return empty array
    return conversations[user] ? conversations[user] : [];
}

function saveConversationForUser(user, conversation) {
    const conversations = loadAllConversations();
    conversations[user] = conversation;
    saveAllConversations(conversations);
}

// called when /clear is sent to eliza
function clearConversationForUser(user) {
    const conversations = loadAllConversations();
    delete conversations[user];
    saveAllConversations(conversations);
}

// easy clean up for testing
function clearAllConversations() {
    localStorage.removeItem(STORAGE_KEY);
}

// active conversation (either starts fresh or is written over by local storage)
let conversation = [];

// track which ones have been used to avoid repeats when randomly choosing one
const usedResponses = [];
const usedQuestions = [];

const DICTIONARY_RESPONSE_COUNT = dictionary.entries.reduce((count, entry) => count + entry.answer.length, 0);
const DICTIONARY_QUESTION_COUNT = dictionary.entries.reduce((count, entry) => count + entry.question.length, 0);

const buildConversationChunk = (chunk) => {
    const article = document.createElement("article");

    if (chunk.message !== '') {
        const message = document.createElement("p");
        message.textContent = `${activeUsername}: ${chunk.message}`;
        article.append(message);
    }

    if (chunk.answer !== '') {
        const answer = document.createElement("p");
        answer.textContent = `Eliza: ${chunk.answer}`;
        article.append(answer);
    }

    if (chunk.question !== '') {
        const question = document.createElement("p");
        question.textContent = `Eliza: ${chunk.question}`;
        article.append(question);
    }

    // append new chunk to the conversation element
    if (article.children.length > 0) {
        const spacer = document.createElement("p");
        spacer.textContent = `---${chunk.timestamp}---`
        article.append(spacer);
        conversationHistory.appendChild(article);
    }


}

function renderConversation() {
    // clear current conversation element
    conversationHistory.innerHTML = "";

    console.log("Active conversation: ", conversation)
    // rerender the conversation chunks in reverse order (newest first)
    for (let i = conversation.length - 1; i >= 0; i--) {
        buildConversationChunk(conversation[i]);
    }
}

// timer to alert user if 10 seconds go by without a response
function runIdleTimer() {
    clearTimeout(idleTimer); // reset timer
    idleTimer = setTimeout(() => {
        alert(getDialogMessage(activeUsername))
        runIdleTimer(); // keep reminding the user until they send something
    }, 10_000);

}

// flag to track if we've moved past the greeting
let greeted = false;
// idle timer
let idleTimer;
// name
let activeUsername;


// helpers
const randomIndex = (n) => Math.floor(Math.random() * n);

const getFirstMatch = (keywords) => {
    for (const entry of dictionary.entries) {
        for (const key of entry.key) {
            if (keywords.includes(key)) {
                return entry;
            }
        }
    }
}

const clearTextInput = () => userInput.value = "";

// randomly choose from possible greeting templates:
const getGreeting = (name) => {
    const options = [
        `Hi, ${name}! How are you?`,
        `Hey ${name}, how is your day going?`,
        `It seems like something is troubling you, ${name}. Want to talk about it?`,
        `Hello ${name}, and welcome to another glorious conversation with a computer. How may I assist you?`
    ]
    greeted = true;
    return options[randomIndex(options.length)];
}

const getDialogMessage = (name) => {
    const options = [
        `Hey ${name}.. I'm waiting!`,
        `${name}? hellloooooooooooooooooo?`,
        `Has anyone seen ${name}??`
    ]

    return options[randomIndex(options.length)];
}

// set active user and load in conversation history
const setUsername = (name) => {
    activeUsername = name;
    activeUserDisplay.textContent = `Active user: ${activeUsername}`;

    // load saved history
    conversation = loadConversationForUser(activeUsername);
    setUsedResponsesAndQuestions();
    renderConversation();
}

// helper to reset conversation-specific state
function setUsedResponsesAndQuestions() {
    usedResponses.length = 0;
    usedQuestions.length = 0; // reset arrays
    for (const chunk of conversation) {
        if (chunk.answer) usedResponses.push(chunk.answer);
        if (chunk.question) usedQuestions.push(chunk.question);
    }
}


const getResponse = (keywords) => {
    // for each word, search for a matching keyword in the dictionary
    const matchedEntry = getFirstMatch(keywords);

    // use first match for simplicity and select answer/question randomly from the options
    if (matchedEntry) {
        console.log("matched:", matchedEntry)
        // get answer and question from matched entry
        const answer = matchedEntry.answer[randomIndex(matchedEntry.answer.length)];
        const question = matchedEntry.question[randomIndex(matchedEntry.question.length)]
        // add to the list of used responses and questions
        usedResponses.push(answer);
        usedQuestions.push(question);
        return {
            answer,
            question
        }
    } else {
        // if there aren't any keyword matches, just pick an answer/question randomly from the dictionary
        let randomEntry = dictionary.entries[randomIndex(dictionary.entries.length)];
        let answer = randomEntry.answer[randomIndex(randomEntry.answer.length)];
        let question = randomEntry.question[randomIndex(randomEntry.question.length)];

        // check to make sure the random choices haven't been used or if everything has been used at least once
        let tries = 0; // limit tries before just picking a random one even if it has been used before to avoid hangups
        while (usedResponses.length < DICTIONARY_RESPONSE_COUNT && usedResponses.includes(answer) ||
        (usedQuestions.length < DICTIONARY_QUESTION_COUNT && usedQuestions.includes(question))
        & tries < 100) {
            randomEntry = dictionary.entries[randomIndex(dictionary.entries.length)];
            answer = randomEntry.answer[randomIndex(randomEntry.answer.length)];
            question = randomEntry.question[randomIndex(randomEntry.question.length)];
            tries++;
        }

        usedResponses.push(answer);
        usedQuestions.push(question);
        return {
            answer,
            question
        }
    }
}


const handleGreeting = () => {
    const name = userInput.value.trim() || "user"; // fallback to default user
    setUsername(name);

    elizaQuestion.textContent = getGreeting(activeUsername);

    clearTextInput();
}

const addConversationChunk = (chunk) => {
    conversation.push(chunk);
    saveConversationForUser(activeUsername, conversation); // save updates to local storage
    renderConversation(); // refresh conversation element
}

const handleConversation = () => {
    const message = userInput.value;
    // break up text from user into words (array of strings)
    const keywords = message.split(" "); // split each word into an entry
    const response = getResponse(keywords);

    // timestamp
    const now = new Date().toLocaleString();
    // record last message
    addConversationChunk({
        message: userQuestion.textContent,
        answer: elizaAnswer.textContent,
        question: elizaQuestion.textContent,
        timestamp: now
    })

    // update fields with current conversation
    userQuestion.textContent = `${activeUsername.toUpperCase()}: ` + userInput.value || "";
    elizaAnswer.textContent = `ELIZA: ` + response.answer;
    elizaQuestion.textContent = "ELIZA: " + response.question;
    clearTextInput();
    runIdleTimer();
}