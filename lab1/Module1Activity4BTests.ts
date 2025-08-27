import {Calculator} from "./Module1Activity4B";

const calculator = new Calculator(0);

// test undo

// add some operations
console.log("Test undo: ");
calculator.calc('{ "operation": "add", "operand": 5 }');
calculator.calc('{ "operation": "subtract", "operand": 3 }');
console.log(`Current result: ${calculator.result}`);
calculator.printMe();
// run undo
console.log("running undo...");
calculator.undo();
// verify last result removed
console.log(`Current result: ${calculator.result}`);
calculator.printMe();
console.log("---------------------------\n\n")

// undo on empty result stack
console.log("Test undo on empty result stack: ")
calculator.undo();
console.log(`Current result: ${calculator.result}`);
calculator.printMe();
calculator.undo();
console.log(`Current result: ${calculator.result}`);
calculator.printMe();
console.log("---------------------------\n\n")


// undo on empty result stack with init value other than 0
console.log("Test undo with init value other than 0 : ")
console.log("Nonzero initial value: ")
const nonZeroCalculator = new Calculator(4);
console.log(`Current result: ${nonZeroCalculator.result}`);
calculator.printMe();
nonZeroCalculator.undo();
console.log(`Current result: ${nonZeroCalculator.result}`);
calculator.printMe();
console.log("---------------------------\n\n")

// test peek
console.log("Test peek: ")
calculator.calc('{ "operation": "add", "operand": 5 }');
calculator.calc('{ "operation": "subtract", "operand": 3 }');
calculator.printMe();
console.log("peek no args: ", calculator.peek());
console.log("peek n(1): ", calculator.peek(1));
// bad index
console.log("peek bad index: ", calculator.peek(99));
console.log("---------------------------\n\n")

// test pop, printMe, clear
console.log("Test pop, clear: ")
calculator.calc('{ "operation": "add", "operand": 7 }');
calculator.calc('{ "operation": "subtract", "operand": 10 }');
console.log("pop:", calculator.pop())
calculator.printMe();
calculator.clear();
calculator.printMe();
console.log("---------------------------\n\n")
