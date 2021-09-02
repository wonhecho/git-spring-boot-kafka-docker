package net.cho.api.quiz.domain;

public enum Factor {
    MINIMUN, MAXIMUM;

    @Override
    public String toString() {
        String value = "";
        switch (this){
            case MAXIMUM:value = String.valueOf(99); break;
            case MINIMUN:value = String.valueOf(11); break;

        }
        return value;
    }
}
