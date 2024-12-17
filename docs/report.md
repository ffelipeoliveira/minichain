### Ver 0.2b report
Keys are now stored under Documents/minichain_keys/ for better acessibility
Your public and private keys are not automaticaly loaded and created if it doesn't exist yet
Using the File java library you can acess internal storage
Tokens still are "created" by using a public key for the Blockchain
Fees were implemented by deducing them from the main amount of tokens and giving them to the miners
The current fee is 1% of every transaction on the list
An calculator has been implemented to make help with calculations while using the app
An unimplemented method "unFork" has been added to App.java, but not yet used
A "Load" class has been implemented but hasn't been completed yet
I have plans for making a way to store the Blockchain locally and making a local network for computers to interact with eachother
Basically I want this to be a proper Blockchain
