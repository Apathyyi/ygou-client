package sy.bishe.ygou.web;

import sy.bishe.ygou.utils.logger.YGouLogger;

class UndefinedEvent extends Event {
    @Override
    public String execute(String params) {
        YGouLogger.e("UndefinedEvent",params);
        return null;
    }
}
