import { Territory } from 'app/shared/model/spr.model';

export const convertTerritory = ({ spr }) => {
  switch (spr.territory) {
    case Territory.CETC_WEST:
      return 'CW-' + spr.number;
    case Territory.HUDSON:
      return 'AH-' + spr.number;
    case Territory.SED:
      return 'SED-' + spr.number;
    case Territory.NED:
      return 'NED-' + spr.number;
    default:
      return 'UN-' + spr.number;
  }
};

export const paginateEntities = (entities, len) => {
  if (len > entities.length) return entities;

  let curLoc = 0;
  let done = false;
  let count = 0;
  const pages = [];

  while (!done) {
    const tmpList = [];
    while (len < count) {
      if (entities[curLoc] === undefined) {
        done = true;
        break;
      }
      tmpList.push(entities[curLoc]);
      curLoc ++;
      count++;
    }
    pages.push(tmpList);
  }

  return pages;
};
