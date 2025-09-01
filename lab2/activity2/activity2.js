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

// event listeners
sendButton.addEventListener("click", (event) => {
    event.preventDefault(); // stop the form from submitting and refreshing the page on click
    clearTimeout(idleTimer); // reset timer
    greeted ? handleConversation() : handleGreeting();
})

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

const setUsername = (name) => {
    activeUsername = name;
    activeUserDisplay.textContent = `Active user: ${activeUsername}`;
}


const getResponse = (keywords) => {
    // for each word, search for a matching keyword in the dictionary
    const matchedEntry = getFirstMatch(keywords);

    // use first match for simplicity and select answer/question randomly from the options
    if (matchedEntry) {
        console.log("matched:", matchedEntry)
        return {
            answer: matchedEntry.answer[randomIndex(matchedEntry.answer.length)],
            question: matchedEntry.question[randomIndex(matchedEntry.question.length)]
        }
    } else {
        // if there aren't any keyword matches, just pick an answer/question randomly from the dictionary
        const randomEntry = dictionary.entries[randomIndex(dictionary.entries.length)];

        return {
            answer: randomEntry.answer[randomIndex(randomEntry.answer.length)],
            question: randomEntry.question[randomIndex(randomEntry.question.length)]
        }
    }
}


const handleGreeting = () => {
    const name = userInput.value.trim() || "user"; // fallback to default user
    setUsername(name);
    elizaQuestion.textContent = getGreeting(activeUsername);
    clearTextInput();
}

const handleConversation = () => {
    // break up text from user into words (array of strings)
    const keywords = userInput.value.split(" "); // split each word into an entry
    const response = getResponse(keywords);

    userQuestion.textContent = `${activeUsername.toUpperCase()}: ` + userInput.value || "";
    elizaAnswer.textContent = `ELIZA: ` + response.answer;
    elizaQuestion.textContent = "ELIZA: " + response.question;
    clearTextInput();
    runIdleTimer();
}