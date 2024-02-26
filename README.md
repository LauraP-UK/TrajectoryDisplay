# TrajectoryDisplay

This project has been extracted from a larger project in order to showcase rendering a trajectory arc for players who are holding a throwable item in Minecraft.
It is not up to date with current Minecraft specs, it is not expected to compile in its current state, due to it being an older version of the larger project.<br>

Examples of this project in action can be seen here:<br>
Simple Arc for consistent velocity items (Snowballs, Enderpearls, Eggs...): https://youtu.be/SrXF0cJ690g?si=EWIgUiFxC4dubZRs<br>
Dynamic Arc for variable velocity items (Arrows): https://youtu.be/zpDy1PJ6yes?si=FJqR8IObU4jlk2rh<br>
<br>
Notes:<br>
This actually draws two trjectories from the player, one from their eyes and one from just to the right of their eyes. This allows blending of the two trajectories together from the right, slowly to the eye-alligned one in order to not obscure the player's vision while aiming with redstone particles in their face. Ideally, replacing the redstone particles with some single-tick particles, from a resource pack or otherwise, would help to reduce the lingering trail.<br>
Also the target zone is a rotating red X in a circle, growing and shrinking in size to draw the eye to where the object will impact. In some instances this becomes invisible due to the impact point being too far away for Minecraft to render.
