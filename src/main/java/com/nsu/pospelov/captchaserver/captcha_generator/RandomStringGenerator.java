package com.nsu.pospelov.captchaserver.captcha_generator;

import java.util.Date;
import java.util.Random;

public class RandomStringGenerator {

    private Random randomGenerator;

    private final String numeric = "0123456789";
    private final String alphabetic = "abcdefghijklmnopqrstuvwxyz";
    private final String characters = numeric + alphabetic;

    public RandomStringGenerator(){

        randomGenerator = new Random((new Date()).getTime()); //Date используется чтобы генерируемые последовательности не повторялись

    }

    public String generateString(int length){

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < length - 2; i++){
            stringBuilder.append(characters.charAt(randomGenerator.nextInt(characters.length())));
        }

        stringBuilder.insert(randomGenerator.nextInt(stringBuilder.length()),  numeric.charAt(randomGenerator.nextInt(numeric.length()))); //капча должна содержать как минимум одну цифру
        stringBuilder.insert(randomGenerator.nextInt(stringBuilder.length()),  alphabetic.charAt(randomGenerator.nextInt(alphabetic.length()))); //капча должна содержать как минимум одну букву


        return stringBuilder.toString();

    }

}
