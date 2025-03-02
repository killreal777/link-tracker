package backend.academy.bot.service.handler.impl.track;

import backend.academy.bot.model.CommandState;

public enum TrackLinkCommandState implements CommandState {
    INPUT_LINK,
    INPUT_FILTERS,
    INPUT_TAGS
}
