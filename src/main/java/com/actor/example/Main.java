package com.actor.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import scala.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("first-actor-system");

        ActorRef wordCountRef = system.actorOf(Props.create(WordCountActor.class), "word-count-actor");
        Future<Object> ask = Patterns.ask(wordCountRef, new WordCountActor.CountWords("Count#Number#of#Words#Of#This#Line", '#'), 1000);

        /*CompletableFuture<Object> ask12 = PatternsCS.ask(wordCountRef, new WordCountActor.CountWords("Count#Words#Of#This#Line", '#'), 1000).toCompletableFuture();
        ask12.get()*/

        /* Adding comment here*/

        ask.onComplete(new OnComplete<Object>() {
            @Override
            public void onComplete( Throwable failure,  Object success) throws Throwable, Throwable {
                int words = (Integer) success;
                System.out.println("words : "+ words);
            }
        }, system.dispatcher());



        ActorRef wordCountRef1 = system.actorOf(Props.create(WordCountActor.class), "word-count-actor-new");
        Future<Object> ask12 = Patterns.ask(wordCountRef1, new WordCountActor.CountWords("Count#Number#of#Words#Of#This#Line#ONE", '#'), 1000);
        ask12.onComplete(new OnComplete<Object>() {
            @Override
            public void onComplete( Throwable failure,  Object success) throws Throwable, Throwable {

                List<Integer> testList = new ArrayList<Integer>();
                System.out.prinlln(testList.toString());
                int words = (Integer) success;
                System.out.println("words : "+ words);
            }
        }, system.dispatcher());


        // system.terminate();



    }
}
