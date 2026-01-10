package cftc.model;

public class InstrumentCategoryVisitable {

	public static <T> T accept(InstrumentCategory category, InstrumentCategoryVisitor<T> visitor) throws Exception {
		
		String cat = category.name();
		T t = null;
		
		switch (cat) {
			case "METAL": t = visitor.visitMetal(); break;
			case "ENERGY": t = visitor.vistEnergy(); break;
			case "FOREX": t = visitor.visitForex(); break;
			default: throw new RuntimeException("Invalid category:" + cat);
		}
		
		return t;
		
	}
}
