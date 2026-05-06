package com.example.traffic.core.phase;

import com.example.traffic.core.model.Movement;
import com.example.traffic.core.model.Road;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class DefaultPhases {
    public static final String NS_STRAIGHT_RIGHT = "NS_STRAIGHT_RIGHT";
    public static final String EW_STRAIGHT_RIGHT = "EW_STRAIGHT_RIGHT";
    public static final String N_LEFT = "N_LEFT";
    public static final String S_LEFT = "S_LEFT";
    public static final String E_LEFT = "E_LEFT";
    public static final String W_LEFT = "W_LEFT";

    private DefaultPhases() {
    }

    public static List<Phase> createDefault() {
        int minGreenSteps = 2;
        int maxGreenSteps = 8;
        int yellowSteps = 1;
        int allRedSteps = 1;
        int baseWeight = 1;

        return List.of(
                new Phase(
                        NS_STRAIGHT_RIGHT,
                        orderedMovements(
                                new Movement(Road.NORTH, Road.SOUTH),
                                new Movement(Road.SOUTH, Road.NORTH),
                                new Movement(Road.NORTH, Road.WEST),
                                new Movement(Road.SOUTH, Road.EAST)
                        ),
                        minGreenSteps,
                        maxGreenSteps,
                        yellowSteps,
                        allRedSteps,
                        baseWeight
                ),
                new Phase(
                        EW_STRAIGHT_RIGHT,
                        orderedMovements(
                                new Movement(Road.EAST, Road.WEST),
                                new Movement(Road.WEST, Road.EAST),
                                new Movement(Road.EAST, Road.NORTH),
                                new Movement(Road.WEST, Road.SOUTH)
                        ),
                        minGreenSteps,
                        maxGreenSteps,
                        yellowSteps,
                        allRedSteps,
                        baseWeight
                ),
                new Phase(
                        N_LEFT,
                        orderedMovements(new Movement(Road.NORTH, Road.EAST)),
                        minGreenSteps,
                        maxGreenSteps,
                        yellowSteps,
                        allRedSteps,
                        baseWeight
                ),
                new Phase(
                        S_LEFT,
                        orderedMovements(new Movement(Road.SOUTH, Road.WEST)),
                        minGreenSteps,
                        maxGreenSteps,
                        yellowSteps,
                        allRedSteps,
                        baseWeight
                ),
                new Phase(
                        E_LEFT,
                        orderedMovements(new Movement(Road.EAST, Road.SOUTH)),
                        minGreenSteps,
                        maxGreenSteps,
                        yellowSteps,
                        allRedSteps,
                        baseWeight
                ),
                new Phase(
                        W_LEFT,
                        orderedMovements(new Movement(Road.WEST, Road.NORTH)),
                        minGreenSteps,
                        maxGreenSteps,
                        yellowSteps,
                        allRedSteps,
                        baseWeight
                )
        );
    }

    private static Set<Movement> orderedMovements(Movement... movements) {
        return new LinkedHashSet<>(List.of(movements));
    }
}
