package cftc.dao;

public interface CftcQueryCallback<T, R> {

	public R accept(T t) throws Exception;
}
