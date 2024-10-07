package cryptographycards;

import java.util.Random;

public class Deck {
	public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
	public static Random gen = new Random();

	public int numOfCards; // contains the total number of cards in the deck
	public Card head; // contains a pointer to the card on the top of the deck

	/*
	 * TODO: Initializes a Deck object using the inputs provided
	 */
	public Deck(int numOfCardsPerSuit, int numOfSuits) {

		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
			throw new IllegalArgumentException("Number of cards per suit is not between 1 and 13");
		}

		if (numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
			throw new IllegalArgumentException("Number of suits is invalid");
		}

		//AC will always be the first in the deck
		this.head = new PlayingCard("clubs", 1);
		numOfCards = 1;

		//starting off with our head
		Card current = head;

		//add a suit until you reach numOfSuits
		for (int i = 0; i < numOfSuits; i++) {
			//add a card to each suit until you reach input size
			for (int j = 1; j <= numOfCardsPerSuit; j++) {
				//AC is already created
				if (i == 0 && j == 1) {
					continue;
				}

				//make a new card next to the current card. ex: in the first iteration next to AC, 2nd next to 2C
				current.next = new PlayingCard(suitsInOrder[i], j);

				//make the prev of the newly added card the card before it. ex: 1st iteration prev is now AC
				current.next.prev = current;

				//pointer now points at new card in the deack
				current = current.next;

				//increase the size of the deck
				numOfCards++;
			}

		}

		//always add a red joker and black joker to the bottom of the deck and the tail should be the black joker
		current.next = new Joker("red");
		current.next.prev = current;
		current = current.next;
		numOfCards++;

		current.next = new Joker("black");
		current.next.prev = current;
		current = current.next;
		numOfCards++;

		//current here points to black joker make it circle back to the head
		current.next = head;
		//make head point to tail (black joker)
		head.prev = current;
	}

	/*
	 * TODO: Implements a copy constructor for Deck using Card.getCopy().
	 * This method runs in O(n), where n is the number of cards in d.
	 */
	public Deck(Deck d) {
		this.numOfCards = d.numOfCards; //number of cards in the copy deck is the same as cards in the deck d

		this.head = d.head.getCopy(); //set the head of the copy deck to be the head of the deck d

		Card curr = this.head; //our pointer in our copy now points to the head

		Card curr2 = d.head.next; //make the pointer now point to the next card since we already copies the first card

		for (int i = 0; i < d.numOfCards - 1; i++) {
			curr.next = curr2.getCopy();
			curr.next.prev = curr;
			curr = curr.next;
			curr2 = curr2.next;
		}

		//last card in the deck circles back to the head
		curr.next = this.head;

		//head referes to the last card in the deck
		this.head.prev = curr;
	}

	/*
	 * For testing purposes we need a default constructor.
	 */
	public Deck() {

	}

	/*
	 * TODO: Adds the specified card at the bottom of the deck. This
	 * method runs in $O(1)$.
	 */
	public void addCard(Card c) {
		//edge case if deck is empty
		if (head == null) {
			//make the card point to itself
			head = c;
			head.next = c;
			head.prev = c;
			numOfCards++;

		} else {
			head.prev.next = c;
			c.prev = head.prev;
			head.prev = c;
			c.next = head;
			numOfCards++;
		}
	}

	/*
	 * TODO: Shuffles the deck using the algorithm described in the pdf.
	 * This method runs in O(n) and uses O(n) space, where n is the total
	 * number of cards in the deck.
	 */
	public void shuffle() {

		if (numOfCards == 0) {
			return;
		}

		//make an array of cards
		Card[] cards = new Card[numOfCards];
		Card current = head;

		//step1: copy cards from the deck
		for (int i = 0; i < numOfCards; i++) {
			cards[i] = current;
			current = current.next;
		}

		/*step2: Shuffle the array using the following algorithm:
		for i from n-1 to 1 do
			j <-- random integer such that 0 <= j <= i
		swap a[j] and a[i]*/
		//from n-1 being the number of cards until 1 inclusive
		//

		int n = numOfCards;
		for (int i = n - 1; i > 0; i--) {
			//upper bound includes i
			int j = gen.nextInt(i + 1);
			Card temp = cards[j];
			cards[j] = cards[i];
			cards[i] = temp;
		}

		//step3:'
		//make new card after shuffling the new head
		head = cards[0];
		current = head;

		for (int i = 1; i < numOfCards; i++) {
			current.next = cards[i];
			current.next.prev = current;
			current = current.next;
		}

		//make the array circular, where current is now the tail points to head
		//and head.prev points to the tail
		current.next = head;
		head.prev = current;
	}

	/*
	 * TODO: Returns a reference to the joker with the specified color in
	 * the deck. This method runs in O(n), where n is the total number of
	 * cards in the deck.
	 */
	public Joker locateJoker(String color) {
		Card current = head;

//		Card cur = head;
//
//		System.out.println("new deck");
//		for (int i = 0; i < numOfCards; i++){
//			System.out.println(cur.toString());
//			cur = cur.next;
//		}


		for (int i = 0; i < numOfCards; i++) {
			if (current instanceof Joker && ((Joker) current).getColor().equalsIgnoreCase(color)) {
				return (Joker) current;
			}
			current = current.next;
		}
		return null;
	}

	/*
	 * TODO: Moved the specified Card, p positions down the deck. You can
	 * assume that the input Card does belong to the deck (hence the deck is
	 * not empty). This method runs in O(p).
	 */
	public void moveCard(Card c, int p) {
		Card current = c;
		Card card2 = current.next;
		Card card1 = current.prev;

		if (p > numOfCards){
			p = p % numOfCards + 1;
		}

		for (int i = 0; i < p; i++){
			current = current.next;

			if (current == null){
				current = head;
			}
		}

		card1.next = card2;
		card2.prev = card1;

		Card card3 = current;
		Card card4 = current.next;

		card3.next = c;
		c.next = card4;

		card4.prev = c;
		c.prev = card3;
	}


	/*
	 * TODO: Performs a triple cut on the deck using the two input cards. You
	 * can assume that the input cards belong to the deck and the first one is
	 * nearest to the top of the deck. This method runs in O(1)
	 */
	public void tripleCut(Card firstCard, Card secondCard) {

		if (firstCard == head) {
			//card after the first card is the tail
			//so have pointers to tail and card after the second card
			Card t = firstCard.prev;
			Card n = secondCard.next;

			//card after the second card is now the head
			head = n;

			//connect the first card to the old tail
			firstCard.prev = t;
			t.next = firstCard;

			//connect new tail (second joker) to head
			head.prev = secondCard;
			secondCard.next = head;


		} else if (secondCard == head.prev) {
			Card n = head;
			Card t = firstCard.prev;

			secondCard.next = n;
			n.prev = secondCard;

			head = firstCard;
			head.prev = t;
			t.next = head;


		} else {
			//have 4 pointers to the head, card before first card, card after second card and tail
			Card A = head;
			Card B = firstCard.prev;
			Card C = secondCard.next;
			Card D = head.prev;

			//change pointer of head so that it now points to second card and 2nd card points to what was the head
			secondCard.next = A;
			A.prev = secondCard;

			//change pointer of tail so that it points to the first card
			firstCard.prev = D;
			D.next = firstCard;

			//update the new head and make it point to the set of cards that is now at the end
			head = C;
			head.prev = B;
			B.next = head;

			/*C = head;

			D = firstCard.prev;
			D.next = firstCard;

			A = secondCard.next;
			A.prev = secondCard;

			B = head.prev;
			B.next = C;*/
		}

	}

	/*
	 * TODO: Performs a count cut on the deck. Note that if the value of the
	 * bottom card is equal to a multiple of the number of cards in the deck,
	 * then the method should not do anything. This method runs in O(n).
	 */
	public void countCut() {
		int cut = (head.prev.getValue()) % numOfCards;
		Card cutCard = head;
		Card tail = head.prev;

		if (cut != 0) {
			//case when the method does nothing
			if ((head.prev.getValue()) == numOfCards - 1) {
				return;
			}
			for (int i = 0; i < cut; i++) {
				cutCard = cutCard.next;
			}

			tail.prev.next = head;
			head.prev = tail.prev;

			tail.prev = cutCard.prev;
			cutCard.prev.next = tail;

			head = cutCard;
			head.prev = tail;
			tail.next = head;
		}
	}

	/*
	 * TODO: Returns the card that can be found by looking at the value of the
	 * card on the top of the deck, and counting down that many cards. If the
	 * card found is a Joker, then the method returns null, otherwise it returns
	 * the Card found. This method runs in O(n).
	 */
	public Card lookUpCard() {
		int count = head.getValue();

		Card current = head;

		for (int i = 0; i < count; i++) {
			current = current.next;
		}

		if (current instanceof Joker) {
			return null;

		} else {
			return current;
		}
	}

	/*
	 * TODO: Uses the Solitaire algorithm to generate one value for the keystream
	 * using this deck. This method runs in O(n).
	 */
	public int generateNextKeystreamValue() {
		//STEP 1
		// locate the red joker and move it one card down to the place of next card
		//if the joker is a tail move to be after the head

		//STEP2
		//locate the black joker and move it down two cards next next
		//if black joker is a bottom card move it so it is below the 2nd card from the head

		Joker redJoker = locateJoker("red");
		Joker blackJoker = locateJoker("black");

		moveCard(redJoker, 1);
		moveCard(blackJoker, 2);

		//STEP 3
		// perform a triple cut at the first and second joker
		// it does not matter the color just use the card that comes first
		Card current = head;
		Card firstJoker = null;

		for (int i = 0; i < numOfCards; i++) {
			if (current instanceof Joker) {
				firstJoker = (Joker) current;
				break;
			}
			current = current.next;
		}

		//using the firstJoker determine the order of the triple cut and perform it
		if (redJoker == firstJoker) {
			tripleCut(redJoker, blackJoker);
		} else {
			tripleCut(blackJoker, redJoker);
		}

		//STEP 4
		// perform a count cut
		countCut();

		//STEP 5
		// look at the value of the head
		Card look = lookUpCard();
		if (look == null) {
			generateNextKeystreamValue();
		}
		return lookUpCard().getValue();
	}

	public abstract class Card {
		public Card next;
		public Card prev;

		public abstract Card getCopy();
		public abstract int getValue();

	}

	public class PlayingCard extends Card {
		public String suit;
		public int rank;

		public PlayingCard(String s, int r) {
			this.suit = s.toLowerCase();
			this.rank = r;
		}

		public String toString() {
			String info = "";
			if (this.rank == 1) {
				//info += "Ace";
				info += "A";
			} else if (this.rank > 10) {
				String[] cards = {"Jack", "Queen", "King"};
				//info += cards[this.rank - 11];
				info += cards[this.rank - 11].charAt(0);
			} else {
				info += this.rank;
			}
			//info += " of " + this.suit;
			info = (info + this.suit.charAt(0)).toUpperCase();
			return info;
		}

		public PlayingCard getCopy() {
			return new PlayingCard(this.suit, this.rank);
		}

		public int getValue() {
			int i;
			for (i = 0; i < suitsInOrder.length; i++) {
				if (this.suit.equals(suitsInOrder[i]))
					break;
			}

			return this.rank + 13*i;
		}

	}

	public class Joker extends Card{
		public String redOrBlack;

		public Joker(String c) {
			if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
				throw new IllegalArgumentException("Jokers can only be red or black");

			this.redOrBlack = c.toLowerCase();
		}

		public String toString() {
			//return this.redOrBlack + " Joker";
			return (this.redOrBlack.charAt(0) + "J").toUpperCase();
		}

		public Joker getCopy() {
			return new Joker(this.redOrBlack);
		}

		public int getValue() {
			return numOfCards - 1;
		}

		public String getColor() {
			return this.redOrBlack;
		}
	}

}
