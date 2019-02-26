package com.thoughtworks.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.program.MerchantGuide;

public class MerchantGuideTest {

	MerchantGuide merchantGuide = new MerchantGuide();
	Map<String, String> unitRomanLetterMap = new HashMap<>();
	Map<String, Double> metalValueMap = new HashMap<>();
	
	@Before
	public void init() {
		unitRomanLetterMap.put("glob", "I");
		unitRomanLetterMap.put("pish", "X");
		merchantGuide.handleCreditCalculation("pish Gold is 57800 Credits", unitRomanLetterMap, metalValueMap);
    }
	
	@Test
    public void testUnitMapping() {
		merchantGuide.handleUnitToRomanLetterMapping("prok is V", unitRomanLetterMap);
		assertEquals(3, unitRomanLetterMap.size());
		merchantGuide.handleUnitToRomanLetterMapping("prok is A", unitRomanLetterMap);
		assertEquals(3, unitRomanLetterMap.size());
		merchantGuide.handleUnitToRomanLetterMapping("tegj is I", unitRomanLetterMap);
		assertEquals(4, unitRomanLetterMap.size());
		merchantGuide.handleUnitToRomanLetterMapping("tegj is L", unitRomanLetterMap);
		assertEquals(4, unitRomanLetterMap.size());
    }
	
	@Test
	public void testCreditCalculation() {
		merchantGuide.handleCreditCalculation("glob glob Silver is 34 Credits", unitRomanLetterMap, metalValueMap);
		assertEquals(2, metalValueMap.size());
		assertEquals("17.0", metalValueMap.get("Silver").toString());
		merchantGuide.handleCreditCalculation("glob Gold glob is 34 Credits", unitRomanLetterMap, metalValueMap);
		assertEquals("34.0", metalValueMap.get("Gold").toString());
	}
	
	@Test
	public void testInputQuestions() {
		assertEquals("pish glob glob is 12", merchantGuide.handleInputQuestions("how much is pish glob glob ?", unitRomanLetterMap, metalValueMap));
		assertEquals("glob Gold is 5780 Credits", merchantGuide.handleInputQuestions("how many Credits is glob Gold ?", unitRomanLetterMap, metalValueMap));
		assertEquals("glob Diamond is 1", merchantGuide.handleInputQuestions("how many Credits is glob Diamond ?", unitRomanLetterMap, metalValueMap));
	}
	
	@Test
    public void testRomanLetterToInt(){
        assertEquals(4, merchantGuide.convertRomanLetterToInt("IV"));
        assertEquals(1903, merchantGuide.convertRomanLetterToInt("MCMIII"));
        assertEquals(0, merchantGuide.convertRomanLetterToInt("MMMMCIII"));
        assertEquals(0, merchantGuide.convertRomanLetterToInt("MMDCMI"));
        assertEquals(0, merchantGuide.convertRomanLetterToInt("AB"));
    }
	
}
