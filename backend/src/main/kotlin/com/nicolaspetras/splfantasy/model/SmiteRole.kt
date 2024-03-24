package com.nicolaspetras.splfantasy.model

/**
 * The role of the player on the website most commonly it will be one of: Solo, Jungle, Mid, Support or Carry.
 * In exceptional cases when a coach or substitute has to fill in the for the regular role player then they will have the
 * role Sub or Coach. None is added in case of a missing role, or a new unexpected role not yet added to the enum.
 */
enum class SmiteRole {
    SOLO,
    JUNGLE,
    MID,
    SUPPORT,
    CARRY,
    SUB,
    COACH,
    NONE
}