Technologies I have used
1. Java 21
2. Springboot
3. gradle

Assumption

I implemented the logic for a single elevator request, where the system selects the nearest idle elevator or an elevator already moving in the same direction.
If I had more time, I would have implemented a queue handler for multiple requests. I already started but couldn't finish it.

Once the user is inside the elevator and the doors close, the elevator moves in the direction originally requested (up/down arrow).

For example, if the elevator is requested at floor 3 with an upward direction, and the user, once inside, 
     presses the buttons for floors 5, 2, 4, and 1, the elevator will first move up to floors 4 and 5 before changing direction to move down to floors 2 and 1.
     After completing all requests, it returns to an idle state.

I would love to 

To start
I have used Intellij and the application run on port 5060
