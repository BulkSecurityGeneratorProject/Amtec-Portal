import { Resolution } from 'app/shared/model/spr.model';

export const getUserReviewed = (user, sprList) => {
  const reviewedSprs = [];

  sprList.forEach(spr => {
    if (spr.user === user && spr.resolution === Resolution.REVIEWED) {
      reviewedSprs.push(spr);
    }
  });
  return reviewedSprs;
};

export const getUserReviewedCount = (user, sprList) => {
  let reviewedSprs = 0;

  sprList.forEach(spr => {
    if (spr.user.login === user.login && spr.resolution === Resolution.REVIEWED) {
      reviewedSprs++;
    }
  });
  return reviewedSprs;
};

export const getUserOpen = (user, sprList) => {
  const openSprs = [];
  sprList.forEach(spr => {
    if (spr.user.login === user.login) {
      switch (spr.resolution) {
        case Resolution.NEW:
          openSprs.push(spr);
          break;
        case Resolution.PARTIALLY_FIXED:
          openSprs.push(spr);
          break;
        case Resolution.DUPLICATE:
          openSprs.push(spr);
          break;
        default:
          break;
      }
    }
  });
  return openSprs;
};

export const getUserOpenCount = (user, sprList) => {
  let openSprs = 0;
  sprList.forEach(spr => {
    if (spr.user.login === user.login) {
      switch (spr.resolution) {
        case Resolution.NEW.toString():
          openSprs++;
          break;
        case Resolution.PARTIALLY_FIXED:
          openSprs++;
          break;
        case Resolution.DUPLICATE:
          openSprs++;
          break;
        default:
          break;
      }
    }
  });
  return openSprs;
};

export const getUserPercentage = (user, sprList) => {
  let totalUserSprs = 0;
  sprList.forEach(spr => {
    if (spr.user.login === user.login) {
      totalUserSprs++;
    }
  });
  return {
    'total': sprList.length,
    'totalUser': totalUserSprs,
    'percentage': Math.round((totalUserSprs / sprList.length) * 100)
  };
};

export const cleanUserList = userList => {
  const newList = [];

  userList.forEach(user => {
    switch (user.firstName) {
      case 'Administrator':
        break;
      case 'User':
        break;
      case 'System':
        break;
      default:
        newList.push(user);
        break;
    }
  });
  return newList;
};
