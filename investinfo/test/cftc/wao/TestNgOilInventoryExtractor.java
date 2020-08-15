package cftc.wao;

import java.io.IOException;

import cftc.wao.EiaNgInventoryExtractor;

public class TestNgOilInventoryExtractor {

	public static void main(String[] args) throws IOException {
		
		String[] inventory = EiaNgInventoryExtractor.retrieveNgInventory();
		System.out.println("NG Inventory:" + inventory[0] + ", " + inventory[1]);
		
		String[] oilInventory = EiaOilInventoryExtractor.retrieveOilInventory();
		System.out.println("Oil Inventory:" + oilInventory[0] + ", " + oilInventory[1]);
	}

}
