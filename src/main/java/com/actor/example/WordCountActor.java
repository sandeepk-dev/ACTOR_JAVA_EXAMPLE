package com.actor.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;

public class WordCountActor extends AbstractActor {

    private ActorSystem system;

    public ActorSystem getSystem() {
        return system;
    }
    /* public static Props withProps(ActorSystem actorSystem) {
        System.out.println("Setting props for WordCountActor");
        return Props.create(WordCountActor.class, actorSystem);
    }*/

    public WordCountActor(ActorSystem system) {
        System.out.println("Creating WordCountActor");
        this.system = system;
    }

    public WordCountActor() {
        system  = ActorSystem.create("word-count-actor-system");
    }

    public static class GetMultiplyNumber {
        private int finalNumber;

        public int getFinalNumber() {
            return finalNumber;
        }

        public GetMultiplyNumber(int number) {
            this.finalNumber = number;

        }
    }

    public static class CountWords {
        private String line;
        private Character delimiter;

        public CountWords(String line, Character delimiter) {
            this.line = line;
            this.delimiter = delimiter;
        }

        public String getLine() {
            return line;
        }

        public Character getDelimiter() {
            return delimiter;
        }
    }

    @Override
    public void preStart() {
        System.out.println("Before starting WordCountActor");
    }

    @Override
    public  void  postStop() {
        System.out.println("After stopping WordCountActor ");

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CountWords.class, countWords -> {
            try {
                System.out.println("Received Message for WordCountActor to count words ... ");
                String line = countWords.getLine();
                Character delimiter = countWords.getDelimiter();
                int words = countWordsFormLine(line, delimiter);

                getSender().tell(words, getSelf());

               // ActorSystem system = ActorSystem.create("inside-word-count-actor-system"+words);

                ActorRef multiplierRef = system.actorOf(Props.create(MultiplierActor.class), "multiplier-actor"+words);
                multiplierRef.forward(new MultiplierActor.MultiplierMessage(words), getContext());


                System.out.println("Progressing with my rest of tasks");
            } catch (Throwable t) {
                getSender().tell(new Status.Failure(t), getSelf());
                throw t;
            }
        }).match(GetMultiplyNumber.class, getMultiplyNumber -> {
            System.out.println("Received message of final output with number count :" + getMultiplyNumber.getFinalNumber());

        }).build();
    }

    private int countWordsFormLine(String line, Character delimiter) {
        String[] words = line.split(String.valueOf(delimiter));
        return words.length;
    }
}
