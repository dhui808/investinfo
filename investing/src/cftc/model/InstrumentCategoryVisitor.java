package cftc.model;

public interface InstrumentCategoryVisitor<T> {

	T visitForex() throws Exception;
	T vistEnergy() throws Exception;
	T visitMetal() throws Exception;
}
