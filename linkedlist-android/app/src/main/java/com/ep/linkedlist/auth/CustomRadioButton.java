package com.ep.linkedlist.auth;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;


import com.ep.linkedlist.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class CustomRadioButton {
    private Map<Integer, ImageButton> m_buttons = new HashMap();
    private Map<Integer, Pair<Drawable, Drawable>> m_images = new HashMap();

    private int m_selectedButtonId = 0;

    public void RegistButton(int id, ImageButton button, Drawable normalImage, Drawable pressedImage) {
        m_buttons.put(id, button);
        m_images.put(id, new Pair(normalImage, pressedImage));
    }

    public Drawable GetNormalImage(int id) {
        return m_images.get(id).getFirst();
    }

    public Drawable GetPressedImage(int id) {
        return m_images.get(id).getSecond();
    }

    public void OnClick(int id) {
        if (m_buttons.isEmpty() == false && m_buttons.containsKey(id))
        {
            m_buttons.get(id).setImageDrawable(m_images.get(id).getSecond());
            if (id != m_selectedButtonId &&
                m_buttons.containsKey(m_selectedButtonId)) {
                m_buttons.get(m_selectedButtonId).setImageDrawable(m_images.get(m_selectedButtonId).getFirst());
            }

            m_selectedButtonId = id;
        }
    }
}
