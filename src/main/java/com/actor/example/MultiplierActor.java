package com.actor.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MultiplierActor extends AbstractActor {

    public static class MultiplierMessage {
        private int multiplier;

        public MultiplierMessage(int words) {
           this.multiplier = words;
        }

        public int getMultiplier() {
            return multiplier;
        }
    }

    private void TestMethod() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        System.out.prinlln(list);
        }

    @Override
    public Receive createReceive() {
        return  receiveBuilder().match(MultiplierMessage.class, multiplierMessage -> {
            int finalNumber = multiplierMessage.getMultiplier() * 10;
            System.out.println("Multiplying number with 10 to get .. "+ finalNumber);


            System.out.println(getSelf());
            System.out.println(getSelf().path().parent());

            System.out.println(getSender());


            //just to make system name unique
            ActorSystem system = ActorSystem.create("send-response-system "+finalNumber);
            ActorRef finalResponseRef = system.actorOf(Props.create(WordCountActor.class), "final-response-actor"+finalNumber);
            finalResponseRef.tell(new WordCountActor.GetMultiplyNumber(finalNumber), ActorRef.noSender());
           // System.out.println("Parent Actor is " + getContext().getSystem().;);
        }).build();
    }
}
