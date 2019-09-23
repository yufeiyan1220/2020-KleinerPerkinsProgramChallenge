# 2020 Kleiner Perkins Fellows Program Challenge

#### Challenge Details
Build a console-based (i.e. runs in terminal) unix-compatible interactive card game. You can pick Blackjack, Solitaire, Memory, or any other game of your choosing (please include game rules in this case).

## Card Game Introduction: Fried Golden Flower Card Game
### Introduction
* It is a popular folk card game in China that has a unique card rule. The player wins or loses with three cards in the hand, and the player can compare with the other players at the time of his operation.

### Basic rules
#### Card Requirement
* The number of participants in the game is 2 people, using a pair of playing cards that have been removed Jokers, a total of 52 cards.

#### Game started
1. Each of the 2 players gets the hand with 3 cards from the stack.
2. (Not implemented)The game start with a base ante. Each player could pay for the ante first and could bet with chips, or just pay for the ante and surrender.
3. According to the pattern of hand, player could choose to compare with the opponent or just surrender.
4. Based on the comparing rules, it is to decide who win the game.

#### Pattern of Hand
There are different types of hand with 3 cards, they are:
1. Boomb: 3 cards has the same number. (eg. ♦A ♥A ♠A, ♠2 ♣2 ♦2)
2. Golden Straight: 3 cards belong to the same suit and their number is a straight. (eg. ♣A ♣2 ♣3, ♠Q ♠K ♠A)
3. Golden: 3 cards belong to the same suit. (eg. ♣A ♣10 ♣J, ♠2 ♠4 ♠8)
4. Straight: The number of the three cards is a straight. (eg. ♣A ♠2 ♣3, ♥8 ♠9 ♦10)
5. Pairs: 2 of 3 cards has the same number. (eg. ♣A ♠A ♣3, ♥K ♠2 ♦2)
6. Single: Other patterns.
7. Special Single: The pattern of numer 2 3 5, which is the smallest single pattern in the game. (eg. ♣2 ♠3 ♣5)

#### Compare rule

##### Compare the pattern First
1. Common rules: Single < Pairs < Straight < Golden < Golden Straight < Bomb
2. Special rule: Bomb < Special Single
3. Game could be tied

##### Compare the number of card
1. The basic rule for single, Pairs, Golden and Bomb is:

  2 < 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A

2. The basic rule for straight and Golden straight is:

  A23 < 234 < 345 < 456 < ... < JQK < QKA

3. The special single could just like single unless the case of comparing with Bomb.

4. For Pairs, compare the number of pair first and then the single if the 2 players have the same number of pair pattern.  

5. Regardless of the suit type, and tied game is allowed.

## Usage

### Dependencies (Java)
#### Java version
* JDK: jdk1.8.0_191
* JRE: jre1.8.0_201

#### Running On the IDEA

```bash
cd ./idea_version
```
1. Run the CardServer.
2. Run 2 CardClient with 2 consoles.
3. For each client, follow the game instructions and get the comparing result together with the hand pattern of the opponent.

#### Running On the Console

```bash
cd ./idea_version
#Compile server

#Compile client

```
1. Run the CardServer.
2. Run 2 CardClient with 2 consoles.
3. For each client, follow the game instructions and get the comparing result together with the hand pattern of the opponent.


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
