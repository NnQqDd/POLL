import java.util.*;
import java.math.*;

/*
Map constants, do not perform assignment !
*/
public class MC{
	public final static int GOAL = 0; 
	public final static int SOURCE = 1;
	public final static int NONE = 2;
	public final static int GROUND = 3;
	public final static int STAR = 4;
	public final static int LBCM = 5; //left-bottom corner mirror
	public final static int LTCM = 6; //left-top corner mirror
	public final static int RTCM = 7; //right-top corner mirror
	public final static int RBCM = 8; //right-bottom corner mirror
	public final static int BLOCK = 9; 
	public final static int MAX_VALUE = (int)1e9;
	public static boolean isNull(int i){
		return i < 0 || i > 9;
	}
	public static boolean isPassable(int i){
		return i == NONE || i == GROUND || i == STAR || i == SOURCE;
	}
	public static boolean isTurnable(int i){
		return i == GROUND || i == STAR;
	}
	public static boolean isHalfMirror(int i){
		return i == LBCM || i == LTCM || i == RTCM || i == RBCM;
	}
	public static boolean isFullBlock(int i){
		return i == BLOCK || i == GOAL;
	}
	public static String nameLookup(int id){
		if (id == GOAL) {
		    return "GOAL";
		} else if (id == SOURCE) {
		    return "SOURCE";
		} else if (id == NONE) {
		    return "NONE";
		} else if (id == GROUND) {
		    return "GROUND";
		} else if (id == LBCM) {
		    return "Left-Bottom corner mirror";
		} else if (id == LTCM) {
		    return "Left-Top corner mirror";
		} else if (id == RTCM) {
		    return "Right-Top corner mirror";
		} else if (id == RBCM) {
		    return "Right-Bottom corner mirror";
		} else if (id == BLOCK) {
		    return "BLOCK";
		} else if (id == STAR) {
		    return "STAR";
		}
		return "NULL";
	}
}	