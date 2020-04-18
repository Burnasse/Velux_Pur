package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public interface Entity {


     /*
     fonction permettant avec le path du fichier g3db ( c'est le meilleurs pour
     chargé des models ) et une hitbox ( btCollisionShape est une class permettant
     de creer des boites de collisions plus ou moins precise suivant l'objet) de creer un objet qui est normalement
     directement instanciable en jeux.
      */
     void loadObject(String fileName,btCollisionShape shape);

     InGameObject createObjectFromModel(String node,Model model,btCollisionShape shape);


     /*
     getter de l'objet instanciable
      */
     InGameObject getInGameObject();


     /*
     permet de vider la memoire de la hitbox (a faire obligatoirement
     quand on a finis d'utiliser l'objet car pas automatique )
      */
     void dispose();

}
