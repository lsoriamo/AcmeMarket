package es.us.lsi.acme.market;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.iconics.IconicsDrawable;

import es.us.lsi.acme.market.R;

public class MainAboutActivity extends MaterialAboutActivity {

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(getString(R.string.app_name))
                .desc("© 2018 Acme SuperMarket")
                .icon(R.drawable.acme_logo)
                .build());

        int colorIcon = R.color.colorAccent;
        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                new IconicsDrawable(c)
                        .iconText("V")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56),
                "Versión",
                false));


        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Autores");
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Wile E. Coyote")
                .subText("CEO")
                .icon(new IconicsDrawable(c)
                        .iconText("WC")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Bugs Bunny")
                .subText("CTO")
                .icon(new IconicsDrawable(c)
                        .iconText("CC")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Daffy Duck")
                .subText("CMO")
                .icon(new IconicsDrawable(c)
                        .iconText("DD")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Road Runner")
                .subText("Thinking brain")
                .icon(new IconicsDrawable(c)
                        .iconText("TB")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56))
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();


        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                new IconicsDrawable(c)
                        .iconText("W")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56),
                "Visita nuestra web",
                true,
                Uri.parse("http://www.informatica.us.es")));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                new IconicsDrawable(c)
                        .iconText("C")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56),
                "Envíanos un email",
                true,
                "contact_contact@acmemarket.com",
                "Contacto"));


        MaterialAboutCard.Builder otherCardBuilder = new MaterialAboutCard.Builder();
        otherCardBuilder.title("Más información");

        otherCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .iconText("MI")
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(56))
                .text("Sobre este proyecto")
                .subTextHtml("Este proyecto ha sido desarrollado por Luis Miguel Soria Morillo y Juan Antonio Álvarez García para la asignatura de Tecnologías Móviles del MIS.")
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), convenienceCardBuilder.build(), otherCardBuilder.build());

    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.app_name);
    }
}
