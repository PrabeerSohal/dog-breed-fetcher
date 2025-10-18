package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private BreedFetcher fetcher;
    private Map<String,List<String>> cache;
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (this.cache.containsKey(breed)){
            return this.cache.get(breed);
        }
        try{
            List<String> subBreeds = this.fetcher.getSubBreeds(breed);
            this.cache.put(breed, subBreeds);
            this.callsMade += 1;
        }
        catch (BreedNotFoundException e){
            return new ArrayList<>();
        }
        return new ArrayList<>();

    }

    public int getCallsMade() {
        return callsMade;
    }
}