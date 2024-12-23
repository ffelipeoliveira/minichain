### Ver 0.2c report
I now added block saving using java's serialization
Also I organized a bit more the file management
Keys are now stored on Documents/minichain/keys and blocks on Documents/minichain/blockchain which I think it's easier to understand
Also I did some debugging and organized the code some more.
The calculator still a bit unprevisible when calculating long expressions. I sincerely don't know what to do
All changes are basically just that tbh, which is kinda underwhelming
On the bright side, I'm on my way to the final steps of the development of this project, closer than further.
Now I want to make a p2p local network using some kind of tcp server, implement the unforking & implement the string cryptography
I'm not satisfied with the way things are organized now either. It still very unfriendly. I'll strive for better code and a cleaner workspace.

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