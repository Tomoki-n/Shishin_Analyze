package com.company;

/**
 * Created by Yuta on 2015/09/21.
 */
public class Glyph {

    /**
     * 適当なグリフシーケンスを生成
     * @return グリフシーケンス
     */
    public static String getGlyphSeq() {
        return getGlyphSeq(0.05, 0.1, 0.4, 0.4, 0.05);
    }

    /**
     * 1～5グリフシーケンスの確率を指定してグリフシーケンスを生成する。
     * 確率の和が1になる必要性はない。
     * @param pb1 1グリフシーケンスが出る確率
     * @param pb2 2グリフシーケンスが出る確率
     * @param pb3 3グリフシーケンスが出る確率
     * @param pb4 4グリフシーケンスが出る確率
     * @param pb5 5グリフシーケンスが出る確率
     * @return グリフシーケンス
     */
    public static String getGlyphSeq(double pb1, double pb2, double pb3, double pb4, double pb5) {
        double sum = pb1 + pb2 + pb3 + pb4 + pb5;
        double rand = Math.random() * sum;

        if(rand <= pb1) {
            return getGlyphSeq(1);
        } else if(rand <= (pb1 + pb2)) {
            return getGlyphSeq(2);
        } else if(rand <= (pb1 + pb2 + pb3)) {
            return getGlyphSeq(3);
        } else if(rand <= (pb1 + pb2 + pb3 + pb4)) {
            return getGlyphSeq(4);
        } else {
            return getGlyphSeq(5);
        }
    }

    /**
     * グリフの個数を指定してグリフシーケンスを生成
     * @param seq グリフの個数。1～5
     * @return グリフシーケンス
     */
    public static String getGlyphSeq(int seq) {

        int rand;
        switch (seq) {
            case 1:
                rand = (int)(Math.random() * 113);
                switch (rand) {
                    case 0:
                        return "GAIN";
                    case 1:
                        return "OLD";
                    case 2:
                        return "PAST";
                    case 3:
                        return "DESTROY";
                    case 4:
                        return "BREATHE";
                    case 5:
                        return "LIVE";
                    case 6:
                        return "CIVILIZATION";
                    case 7:
                        return "DEFEND";
                    case 8:
                        return "HELP";
                    case 9:
                        return "PURSUE";
                    case 10:
                        return "WEAK";
                    case 11:
                        return "AVOID";
                    case 12:
                        return "REBEL";
                    case 13:
                        return "SEPARATE";
                    case 14:
                        return "FORGET";
                    case 15:
                        return "CREATE";
                    case 16:
                        return "ADVANCE";
                    case 17:
                        return "DISTANCE";
                    case 18:
                        return "OUTSIDE";
                    case 19:
                        return "PATH";
                    case 20:
                        return "DETERIORATE";
                    case 21:
                        return "COURAGE";
                    case 22:
                        return "MESSAGE";
                    case 23:
                        return "AGAIN";
                    case 24:
                        return "REPEAT";
                    case 25:
                        return "WANT";
                    case 26:
                        return "DISCOVER";
                    case 27:
                        return "SELF";
                    case 28:
                        return "DIE";
                    case 29:
                        return "STABILITY";
                    case 30:
                        return "STAY";
                    case 31:
                        return "SAFETY";
                    case 32:
                        return "NATURE";
                    case 33:
                        return "CONFLICT";
                    case 34:
                        return "SHAPERS";
                    case 35:
                        return "ATTACK";
                    case 36:
                        return "WAR";
                    case 37:
                        return "CHAOS";
                    case 38:
                        return "LIBERATE";
                    case 39:
                        return "SEE";
                    case 40:
                        return "LESS";
                    case 41:
                        return "NOW";
                    case 42:
                        return "PRESENT";
                    case 43:
                        return "NOT";
                    case 44:
                        return "INSIDE";
                    case 45:
                        return "ANSWER";
                    case 46:
                        return "FEAR";
                    case 47:
                        return "HIDE";
                    case 48:
                        return "DESTINY";
                    case 49:
                        return "SIMPLE";
                    case 50:
                        return "MORE";
                    case 51:
                        return "EQUAL";
                    case 52:
                        return "CHANGE";
                    case 53:
                        return "DIFFICULT";
                    case 54:
                        return "SAVE";
                    case 55:
                        return "EVOLUTION";
                    case 56:
                        return "SUCCESS";
                    case 57:
                        return "QUESTION";
                    case 58:
                        return "ESCAPE";
                    case 59:
                        return "COMPLEX";
                    case 60:
                        return "IDEA";
                    case 61:
                        return "CLEAR";
                    case 62:
                        return "RETREAT";
                    case 63:
                        return "BARRIER";
                    case 64:
                        return "DATA";
                    case 65:
                        return "FAILURE";
                    case 66:
                        return "FOLLOW";
                    case 67:
                        return "DANGER";
                    case 68:
                        return "LEAD";
                    case 69:
                        return "POTENTIAL";
                    case 70:
                        return "CONTEMPLATE";
                    case 71:
                        return "USE";
                    case 72:
                        return "SEARCH";
                    case 73:
                        return "NEW";
                    case 74:
                        return "EASY";
                    case 75:
                        return "REACT";
                    case 76:
                        return "HAVE";
                    case 77:
                        return "CAPTURE";
                    case 78:
                        return "JOURNEY";
                    case 79:
                        return "IGNORE";
                    case 80:
                        return "LOSE";
                    case 81:
                        return "IMPROVE";
                    case 82:
                        return "FUTURE";
                    case 83:
                        return "PURE";
                    case 84:
                        return "IMPURE";
                    case 85:
                        return "IMPERFECT";
                    case 86:
                        return "LIE";
                    case 87:
                        return "TOGETHER";
                    case 88:
                        return "RESISTANCE";
                    case 89:
                        return "STRUGGLE";
                    case 90:
                        return "HARM";
                    case 91:
                        return "ENLIGHTENMENT";
                    case 92:
                        return "BALANCE";
                    case 93:
                        return "PERFECTION";
                    case 94:
                        return "HUMAN";
                    case 95:
                        return "BODY";
                    case 96:
                        return "OPEN";
                    case 97:
                        return "ACCEPT";
                    case 98:
                        return "NOURISH";
                    case 99:
                        return "RECHARGE";
                    case 100:
                        return "REPAIR";
                    case 101:
                        return "MIND";
                    case 102:
                        return "SOUL";
                    case 103:
                        return "END";
                    case 104:
                        return "ALL";
                    case 105:
                        return "CLEAR-ALL";
                    case 106:
                        return "OPEN-ALL";
                    case 107:
                        return "STRONG";
                    case 108:
                        return "XM";
                    case 109:
                        return "TRUTH";
                    case 110:
                        return "PORTAL";
                    case 111:
                        return "HARMONY";
                    case 112:
                        return "PEACE";
                }
                return "";
            case 2:
                rand = (int)(Math.random() * 23);
                switch (rand) {
                    case 0:
                        return "CIVILIZATION CHAOS";
                    case 1:
                        return "PURSUE XM";
                    case 2:
                        return "PURSUE TRUTH";
                    case 3:
                        return "SEPARATE WAR";
                    case 4:
                        return "CREATE DANGER";
                    case 5:
                        return "PATH PERFECTION";
                    case 6:
                        return "DISCOVER SAFETY";
                    case 7:
                        return "DISCOVER LIE";
                    case 8:
                        return "DISCOVER PORTAL";
                    case 9:
                        return "ATTACK CHAOS";
                    case 10:
                        return "ATTACK DIFFICULT";
                    case 11:
                        return "LIBERATE XM";
                    case 12:
                        return "QUESTION WAR";
                    case 13:
                        return "LEAD ENLIGHTENMENT";
                    case 14:
                        return "SEARCH POTENTIAL";
                    case 15:
                        return "SEARCH PAST";
                    case 16:
                        return "CAPTURE PORTAL";
                    case 17:
                        return "PURE TRUTH";
                    case 18:
                        return "PURE BODY";
                    case 19:
                        return "PURE HUMAN";
                    case 20:
                        return "NOURISH JOURNEY";
                    case 21:
                        return "ALL CHAOS";
                    case 22:
                        return "OPEN-ALL TRUTH";
                }
                return "";
            case 3:
                rand = (int)(Math.random() * 44);
                switch (rand) {
                    case 0:
                        return "GAIN DIFFICULT BARRIER";
                    case 1:
                        return "GAIN CIVILIZATION PEACE";
                    case 2:
                        return "DESTROY WEAK CIVILIZATION";
                    case 3:
                        return "DESTROY DESTINY BARRIER";
                    case 4:
                        return "CIVILIZATION WAR CHAOS";
                    case 5:
                        return "AVOID DESTINY LIE";
                    case 6:
                        return "AVOID COMPLEX CONFLICT";
                    case 7:
                        return "SEPARATE FUTURE EVOLUTION";
                    case 8:
                        return "PATH PEACE DIFFICULT";
                    case 9:
                        return "AGAIN JOURNEY OUTSIDE";
                    case 10:
                        return "DISCOVER SHAPERS LIE";
                    case 11:
                        return "DISCOVER PURE TRUTH";
                    case 12:
                        return "NATURE PURE DEFEND";
                    case 13:
                        return "WAR CREATE DANGER";
                    case 14:
                        return "WAR DESTROY FUTURE";
                    case 15:
                        return "ATTACK DIFFICULT FUTURE";
                    case 16:
                        return "ATTACK SHAPERS EVOLUTION";
                    case 17:
                        return "LIBERATE PORTAL POTENTIAL";
                    case 18:
                        return "INSIDE MIND FUTURE";
                    case 19:
                        return "FEAR CHAOS XM";
                    case 20:
                        return "CHANGE HUMAN FUTURE";
                    case 21:
                        return "QUESTION SHAPERS CHAOS";
                    case 22:
                        return "QUESTION HIDE TRUTH";
                    case 23:
                        return "QUESTION CONFLICT DATA";
                    case 24:
                        return "ESCAPE IMPURE EVOLUTION";
                    case 25:
                        return "COMPLEX JOURNEY FUTURE";
                    case 26:
                        return "FOLLOW PURE JOURNEY";
                    case 27:
                        return "LEAD RESISTANCE QUESTION";
                    case 28:
                        return "REACT IMPURE CIVILIZATION";
                    case 29:
                        return "CAPTURE SHAPERS PORTAL";
                    case 30:
                        return "JOURNEY INSIDE SOUL";
                    case 31:
                        return "LOSE ATTACK RETREAT";
                    case 32:
                        return "IMPROVE ADVANCE PRESENT";
                    case 33:
                        return "IMPROVE HUMAN SHAPERS";
                    case 34:
                        return "FUTURE EQUAL PAST";
                    case 35:
                        return "TOGETHER PURSUE SAFETY";
                    case 36:
                        return "HARM DANGER AVOID";
                    case 37:
                        return "PERFECTION PATH PEACE";
                    case 38:
                        return "MIND OPEN LIVE";
                    case 39:
                        return "ALL XM LIBERATE";
                    case 40:
                        return "OPEN-ALL PORTAL SUCCESS";
                    case 41:
                        return "XM NOURISH CIVILIZATION";
                    case 42:
                        return "TRUTH NOURISH SOUL";
                    case 43:
                        return "SEE TRUTH NOW";
                }
                return "";
            case 4:
                rand = (int)(Math.random() * 88);
                switch (rand) {
                    case 0:
                        return "GAIN PORTAL ATTACK WEAK";
                    case 1:
                        return "PAST AGAIN PRESENT AGAIN";
                    case 2:
                        return "DESTROY DESTINY HUMAN LIE";
                    case 3:
                        return "DESTROY COMPLEX SHAPERS LIE";
                    case 4:
                        return "RESTRAINT PATH GAIN HARMONY";
                    case 5:
                        return "RESTRAINT FEAR AVOID DANGER";
                    case 6:
                        return "BREATHE AGAIN JOURNEY AGAIN";
                    case 7:
                        return "BREATHE NATURE PERFECTION HARMONY";
                    case 8:
                        return "DEFEND MESSAGE ANSWER IDEA";
                    case 9:
                        return "HELP GAIN CREATE PURSUE";
                    case 10:
                        return "HELP SHAPERS CREATE FUTURE";
                    case 11:
                        return "AVOID XM MESSAGE LIE";
                    case 12:
                        return "SEPARATE WEAK IGNORE TRUTH";
                    case 13:
                        return "FORGET CONFLICT ACCEPT WAR";
                    case 14:
                        return "CREATE DISTANCE IMPURE PATH";
                    case 15:
                        return "CREATE FUTURE NOT WAR";
                    case 16:
                        return "CREATE FUTURE CHANGE DESTINY";
                    case 17:
                        return "ADVANCE CIVILIZATION REPEAT FAILURE";
                    case 18:
                        return "PATH RESTRAINT STRONG SAFETY";
                    case 19:
                        return "DETERIORATE HUMAN WEAK REBEL";
                    case 20:
                        return "COURAGE WAR SHAPERS FUTURE";
                    case 21:
                        return "STAY TOGETHER DEFEND TRUTH";
                    case 22:
                        return "SHAPERS SEE POTENTIAL EVOLUTION";
                    case 23:
                        return "SHAPERS PORTAL MIND RESTRAINT";
                    case 24:
                        return "SHAPERS PAST PRESENT FUTURE";
                    case 25:
                        return "SHAPERS MIND COMPLEX HARMONY";
                    case 26:
                        return "SHAPERS HAVE STRONG PATH";
                    case 27:
                        return "SHAPERS CHAOS PURE HARM";
                    case 28:
                        return "SHAPERS MESSAGE END CIVILIZATION";
                    case 29:
                        return "ATTACK WEAK SHAPERS LIE";
                    case 30:
                        return "ATTACK RESISTANCE PURSUE ENLIGHTENMENT";
                    case 31:
                        return "ATTACK ENLIGHTENMENT PURSUE RESISTANCE";
                    case 32:
                        return "CHAOS BARRIER SHAPERS PORTAL";
                    case 33:
                        return "LIBERATE XM PORTAL TOGETHER";
                    case 34:
                        return "SEE TRUTH SEE FUTURE";
                    case 35:
                        return "LESS CHAOS MORE STABILITY";
                    case 36:
                        return "LESS TRUTH MORE CHAOS";
                    case 37:
                        return "LESS SOUL MORE MIND";
                    case 38:
                        return "INSIDE MIND JOURNEY PERFECTION";
                    case 39:
                        return "HIDE IMPURE HUMAN THOUGHT";
                    case 40:
                        return "SIMPLE CIVILIZATION IMPURE WEAK";
                    case 41:
                        return "SIMPLE TRUTH BREATHE NATURE";
                    case 42:
                        return "SIMPLE MESSAGE COMPLEX IDEA";
                    case 43:
                        return "CHANGE HUMAN POTENTIAL USE";
                    case 44:
                        return "CHANGE FUTURE CAPTURE DESTINY";
                    case 45:
                        return "CHANGE BODY IMPROVE HUMAN";
                    case 46:
                        return "QUESTION TRUTH GAIN FUTURE";
                    case 47:
                        return "ESCAPE SIMPLE HUMAN FUTURE";
                    case 48:
                        return "COMPLEX SHAPERS CIVILIZATION STRONG";
                    case 49:
                        return "CLEAR MIND OPEN MIND";
                    case 50:
                        return "FOLLOW SHAPERS PORTAL MESSAGE";
                    case 51:
                        return "LEAD PURSUE REACT DEFEND";
                    case 52:
                        return "CONTEMPLATE COMPLEX SHAPERS TRUTH";
                    case 53:
                        return "CONTEMPLATE COMPLEX SHAPERS CIVILIZATION";
                    case 54:
                        return "CONTEMPLATE SELF SHAPERS TRUTH";
                    case 55:
                        return "CONTEMPLATE SELF PATH TRUTH";
                    case 56:
                        return "SEARCH TRUTH SAVE CIVILIZATION";
                    case 57:
                        return "SEARCH XM SAVE PORTAL";
                    case 58:
                        return "SEARCH DATA DISCOVER PATH";
                    case 59:
                        return "CAPTURE FEAR DISCOVER COURAGE";
                    case 60:
                        return "JOURNEY INSIDE IMPROVE SOUL";
                    case 61:
                        return "IGNORE HUMAN CHAOS LIE";
                    case 62:
                        return "LOSE DANGER GAIN SAFETY";
                    case 63:
                        return "IMPROVE BODY PURSUE JOURNEY";
                    case 64:
                        return "IMPROVE BODY MIND SOUL";
                    case 65:
                        return "IMPROVE MIND JOURNEY INSIDE";
                    case 66:
                        return "TOGETHER DISCOVER HARMONY EQUAL";
                    case 67:
                        return "STRUGGLE IMPROVE HUMAN SOUL";
                    case 68:
                        return "STRUGGLE DEFEND SHAPERS DANGER";
                    case 69:
                        return "PERFECTION BALANCE SAFETY ALL";
                    case 70:
                        return "HUMAN HAVE IMPURE CIVILIZATION";
                    case 71:
                        return "HUMAN SOUL STRONG PURE";
                    case 72:
                        return "HUMAN PAST PRESENT FUTURE";
                    case 73:
                        return "NOURISH XM CREATE THOUGHT";
                    case 74:
                        return "SOUL REBEL HUMAN DIE";
                    case 75:
                        return "ALL CHAOS INSIDE BODY";
                    case 76:
                        return "CLEAR-ALL OPEN-ALL DISCOVER TRUTH";
                    case 77:
                        return "STRONG IDEA PURSUE TRUTH";
                    case 78:
                        return "STRONG TOGETHER AVOID WAR";
                    case 79:
                        return "STRONG RESISTANCE CAPTURE PORTAL";
                    case 80:
                        return "XM HAVE MIND JOURNEY";
                    case 81:
                        return "XM DIE CHAOS LIVE";
                    case 82:
                        return "TRUTH IDEA DISCOVER XM";
                    case 83:
                        return "PORTAL HAVE TRUTH DATA";
                    case 84:
                        return "PORTAL POTENTIAL CHANGE FUTURE";
                    case 85:
                        return "PORTAL DIE CIVILIZATION DIE";
                    case 86:
                        return "PORTAL CHANGE CIVILIZATION END";
                    case 87:
                        return "PEACE PATH NOURISH PRESENT";
                }
                return "";
            case 5:
                rand = (int)(Math.random() * 77);
                switch (rand) {
                    case 0:
                        return "GAIN TRUTH OPEN HUMAN SOUL";
                    case 1:
                        return "OLD NATURE LESS STRONG NOW";
                    case 2:
                        return "PAST CHAOS CREATE FUTURE HARMONY";
                    case 3:
                        return "PAST PATH CREATE FUTURE JOURNEY";
                    case 4:
                        return "DESTROY LIE INSIDE GAIN SOUL";
                    case 5:
                        return "DESTROY CIVILIZATION END CONFLICT WAR";
                    case 6:
                        return "BREATHE INSIDE XM LOSE SELF";
                    case 7:
                        return "DEFEND DESTINY DEFEND HUMAN CIVILIZATION";
                    case 8:
                        return "DEFEND HUMAN CIVILIZATION XM MESSAGE";
                    case 9:
                        return "DEFEND HUMAN CIVILIZATION SHAPERS LIE";
                    case 10:
                        return "DEFEND HUMAN CIVILIZATION PORTAL DATA";
                    case 11:
                        return "DEFEND HUMAN CIVILIZATION SHAPERS PORTAL";
                    case 12:
                        return "HELP ENLIGHTENMENT CAPTURE ALL PORTAL";
                    case 13:
                        return "HELP RESISTANCE CAPTURE ALL PORTAL";
                    case 14:
                        return "HELP HUMAN CIVILIZATION PURSUE DESTINY";
                    case 15:
                        return "PURSUE PATH OUTSIDE SHAPERS LIE";
                    case 16:
                        return "PURSUE CONFLICT WAR ADVANCE CHAOS";
                    case 17:
                        return "WEAK HUMAN DESTINY DESTROY CIVILIZATION";
                    case 18:
                        return "AVOID CHAOS REPAIR POTENTIAL WAR";
                    case 19:
                        return "AVOID PERFECTION STABILITY HUMAN SELF";
                    case 20:
                        return "AVOID CHAOS AVOID SHAPERS LIE";
                    case 21:
                        return "REBEL THOUGHT EVOLUTION DESTINY NOW";
                    case 22:
                        return "SEPARATE MIND BODY DISCOVER ENLIGHTENMENT";
                    case 23:
                        return "SEPARATE TRUTH LIE SHAPERS FUTURE";
                    case 24:
                        return "FORGET PAST SEE PRESENT DANGER";
                    case 25:
                        return "FORGET WAR SEE DISTANCE HARMONY";
                    case 26:
                        return "CREATE PURE FUTURE NOT WAR";
                    case 27:
                        return "CREATE PURE FUTURE HUMAN CIVILIZATION";
                    case 28:
                        return "CREATE SEPARATE PATH END JOURNEY";
                    case 29:
                        return "ADVANCE CIVILIZATION PURSUE SHAPERS PATH";
                    case 30:
                        return "DISTANCE SELF AVOID HUMAN LIE";
                    case 31:
                        return "COURAGE ATTACK SHAPERS PORTAL TOGETHER";
                    case 32:
                        return "WANT TRUTH PURSUE DIFFICULT PATH";
                    case 33:
                        return "STAY STRONG TOGETHER DEFEND RESISTANCE";
                    case 34:
                        return "SHAPERS PORTAL DATA CREATE CHAOS";
                    case 35:
                        return "SHAPERS PORTAL MESSAGE DESTRO CIVILIZATION";
                    case 36:
                        return "SHAPERS WANT HUMAN MIND FUTURE";
                    case 37:
                        return "SHAPERS LEAD HUMAN COMPLEX JOURNEY";
                    case 38:
                        return "CHAOS WAR CONFLICT DISCOVER PEACE";
                    case 39:
                        return "LIBERATE PORTAL LIBERATE HUMAN MIND";
                    case 40:
                        return "PRESENT CHAOS CREATE FUTURE CIVILIZATION";
                    case 41:
                        return "INSIDE MIND INSIDE SOUL HARMONY";
                    case 42:
                        return "ANSWER QUESTION DISCOVER DIFFICULT TRUTH";
                    case 43:
                        return "HIDE STRUGGLE ADVANCE STRONG TOGETHER";
                    case 44:
                        return "SIMPLE TRUTH SHAPERS DESTROY CIVILIZATION";
                    case 45:
                        return "SIMPLE OLD TRUTH JOURNEY INSIDE";
                    case 46:
                        return "MORE DATA GAIN PORTAL ADVANCE";
                    case 47:
                        return "SAVE HUMAN CIVILIZATION DESTROY PORTAL";
                    case 48:
                        return "QUESTION LESS FORGET ALL LIE";
                    case 49:
                        return "ESCAPE BODY JOURNEY OUTSIDE PRESENT";
                    case 50:
                        return "ESCAPE BODY MIND SELF WANT";
                    case 51:
                        return "CLEAR MIND LIBERATE BARRIER BODY";
                    case 52:
                        return "CONTEMPLATE FUTURE NOT SHAPERS PATH";
                    case 53:
                        return "CONTEMPLATE RESTRAINT DISCOVER MORE COURAGE";
                    case 54:
                        return "USE RESTRAINT FOLLOW EASY PATH";
                    case 55:
                        return "USE MIND USE COURAGE CHANGE";
                    case 56:
                        return "SEARCH DESTINY CREATE PURE FUTURE";
                    case 57:
                        return "EASY PATH FUTURE FOLLOW SHAPERS";
                    case 58:
                        return "REACT REBEL QUESTION SHAPERS LIE";
                    case 59:
                        return "CAPTURE PORTAL DEFEND PORTAL COURAGE";
                    case 60:
                        return "LOSE SHAPERS MESSAGE GAIN CHAOS";
                    case 61:
                        return "PURE HUMAN FAILURE NOW CHAOS";
                    case 62:
                        return "IMPERFECT XM MESSAGE HUMAN CHAOS";
                    case 63:
                        return "IMPERFECT TRUTH ACCEPT COMPLEX ANSWER";
                    case 64:
                        return "HUMAN SHAPERS TOGETHER CREATE DESTINY";
                    case 65:
                        return "HUMAN NOT TOGETHER CIVILIZATION DETERIORATE";
                    case 66:
                        return "RECHARGE PRESENT RECHARGE HUMAN SOUL";
                    case 67:
                        return "REPAIR SOUL LESS HUMAN HARM";
                    case 68:
                        return "CLEAR-ALL THOUGHT PAST PRESENT FUTURE";
                    case 69:
                        return "STRONG TOGETHER WAR TOGETHER DESTINY";
                    case 70:
                        return "STRONG TOGETHER WAR TOGETHER CHAOS";
                    case 71:
                        return "XM PATH FUTURE DESTINY HARMONY";
                    case 72:
                        return "XM CREATE COMPLEX HUMAN DESTINY";
                    case 73:
                        return "PORTAL CREATE DANGER PURSUE SAFETY";
                    case 74:
                        return "PORTAL POTENTIAL HELP HUMAN FUTURE";
                    case 75:
                        return "PORTAL BARRIER DEFEND HUMAN SHAPERS";
                    case 76:
                        return "PORTAL IMPROVE HUMAN FUTURE CIVILIZATION";
                }
                return "";
            default:
                return "";
        }
    }
}
