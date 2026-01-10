package cftc.dao;

public interface CftcUpdateCallback<T> {

	public void accept(T t) throws Exception;
}
