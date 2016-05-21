package model;

/**
 * Created by pawel on 17.05.16.
 */
public class Pair<S, T> {
    private T type;
    private S word;
    public Pair(S word, T type){
        this.word = word;
        this.type = type;
    }

    public<S> S getWord(){
        return (S)word;
    }

    public<T> T getType(){
        return (T)type;
    }
}
