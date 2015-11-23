

public enum ColorEnum {
	GREEN (18, 110, 18),
	BLUE (0, 128, 255),
	RED (149, 23, 23),
	GREY (100, 100, 100),
	YELLOW(255, 255, 204);
	
	public final int r;
	public final int g;
	public final int b;
	
	ColorEnum(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
}