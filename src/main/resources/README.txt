### Environment Details
1. Java 8+
2. Maven - 3.1.1

### Assumptions
### Input data is categorized into four types.

1) Unit to roman letter mapping
2) Units with credit calculation
3) Question's for metal units
4) Invalid inputs


I)	Unit to roman letter mapping
	a)	Mapping of units to roman letters should be processed before the credits conversion processed using these units.
	b)	This format of input line will have ' is' word in their line.
	c)	The word before ' is' is considered as unit and after ' is' should be a roman letter.
	d)  Duplicate unit name will be overridden and that value will considered for upcoming lines

II)	Units with credit calculations
	a)	Credits calculation for metals should be processed after the unit mapping and before the question lines which uses this credit values(Integer).
	b)	This line will have the units(specified in previous lines) separated by space, then followed by metal name and the last word should be 'Credits'.
	c)  The calculation loop will break once its identified the metal name(any non-unit name in this case)
	c)	This line too has ' is' word.  The words before ' is' will be processed with space separator and the word matches the units will be framed as roman letters 
		and the word that doesn't have the units will be considered as Metal name.
	d)  Duplicate credits value will be overridden and that value will considered for upcoming lines

III) Questions for metal units.
	a) Input line with questions should be followed next.
	b) This line will begin with 'how much is ' or 'how many Credits is ' and ends with '?'.
	c) This line has units and metal separated by spaces.
	d) If the metal name is not present then the value for unit to roman conversion will be output.
	e) If the metal name is present, it should be the last word before '?'.  The roman value equivalent to credits will be the output in this case.
	f) If the metal or unit not valid then its considered as 0

IV) Invalid Inputs
	a) Any line that doesn't have question mark at the end of the line and the last word doesn't match with roman letter will be considered a invalid input.
	b) This line could present in any sequence.
