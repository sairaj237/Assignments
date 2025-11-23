import time

# -----------------------------------------
# Simple Restaurant Chatbot
# -----------------------------------------

def chatbot_reply(message):
    message = message.lower()

    # greetings
    if any(word in message for word in ["hi", "hello", "hey"]):
        return "Hello! How can I help you today?"

    # menu request
    if "menu" in message:
        return ("Here is our menu:\n"
                "- Pizza: $8\n"
                "- Burger: $5\n"
                "- Pasta: $7\n"
                "- Coffee: $2")

    # order
    if "order" in message:
        return "Sure! What would you like to order?"

    if "pizza" in message:
        return "Pizza added to your order."

    if "burger" in message:
        return "Burger added to your order."

    if "pasta" in message:
        return "Pasta added to your order."

    if "coffee" in message:
        return "Coffee added to your order."

    # reservation
    if "book" in message or "reservation" in message:
        return "Please tell me the date and time for your reservation."

    # timing
    if "time" in message or "open" in message:
        return "We are open from 9 AM to 11 PM every day."

    # goodbye
    if any(word in message for word in ["bye", "exit", "quit"]):
        return "Goodbye! Have a nice day."

    return "I am not sure about that. You can ask about menu, order, or reservation."


# -----------------------------------------
# Main program loop
# -----------------------------------------
def start_chatbot():
    print("Restaurant Assistant Chatbot")
    print("Type 'bye' to exit.\n")

    while True:
        user_msg = input("You: ")

        if user_msg.lower() in ["bye", "exit", "quit"]:
            print("Bot: Goodbye! Have a nice day.")
            break

        response = chatbot_reply(user_msg)
        time.sleep(0.3)
        print("Bot:", response)


# -----------------------------------------
# Start chatbot
# -----------------------------------------
if __name__ == "__main__":
    start_chatbot()
